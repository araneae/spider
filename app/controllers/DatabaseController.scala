package controllers

import java.io.File
import org.joda.time.DateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import actors._
import akka.pattern.ask
import akka.util.Timeout
import java.util.concurrent.TimeUnit
import enums._
import models.dtos._
import models.repositories._
import services._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Controller
import scala.concurrent.Await
import traits._
import utils._
import play.api.mvc.MultipartFormData
import play.api.mvc.MultipartFormData.FilePart
import play.api.libs.Files.TemporaryFile

object DatabaseController extends Controller with Secured with AkkaActor {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def upload = IsAuthenticated(parse.multipartFormData) { username => implicit request =>
      //logger.info("in DatabaseController.upload...")
      println("in DatabaseController.upload...")
      request.body match {
        case MultipartFormData(dataParts, fileParts, badParts, missingFileParts) =>
             fileParts.map { case FilePart(key, filename, contentType, ref) =>
                 val file = ref.asInstanceOf[TemporaryFile]
                 val uploadFilePath = Configuration.uploadUserTempFilePath(userId, filename)
                 val uploadDir = Configuration.uploadUserTempPath(userId)
                 FileUtil.createPath(uploadDir)
                 file.moveTo(new File(uploadFilePath), true)
             }
             Ok(HttpResponseUtil.success("Successfully Uploaded!"))
        case _ =>  
            BadRequest(HttpResponseUtil.error("Unable to upload file, please try again!"))
      }
  }
  
  def getShareContacts(documentId: Int) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DatabaseController.getShareContacts(${documentId})")
    println(s"in DatabaseController.getShareContacts(${documentId})")
    
    val list = ContactRepository.findAllWithDocumentShareAttributes(userId, documentId)
    val text = Json.toJson(list)
    Ok(text).as(JSON)
  }
  
  def download(documentId: Int) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DatabaseController.download(${documentId})")
    println(s"in DatabaseController.download(${documentId})")
    
    val document = DocumentRepository.find(documentId)
    document match {
      case Some(doc) =>
          val filePath = Configuration.uploadFilePath(doc.physicalName)
          Ok.sendFile(new File(filePath), fileName = _ => doc.fileName)
      case None =>
          NotFound
    }
  }
  
  def get(documentId: Long) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DatabaseController.get(${documentId})")
    println(s"in DatabaseController.get(${documentId})")
    
    val document = DocumentRepository.find(documentId)
    document match {
        case Some(doc) => val json = Json.toJson(doc)
                          Ok(json).as(JSON)
        case None => BadRequest(HttpResponseUtil.error("Unable to find document!"))
    }
  }
  
  def getAll(userTagId: Option[Int]) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DatabaseController.getAll(${userTagId})")
    println(s"in DatabaseController.getAll(${userTagId})")
    
    userTagId match {
      case Some(tagId) =>
                  val list = DocumentTagRepository.findDocumentByUserTagId(userId, tagId)
                  val text = Json.toJson(list)
                  Ok(text).as(JSON)
      case None =>  
                  val list = UserDocumentRepository.findAll(userId)
                  val text = Json.toJson(list)
                  Ok(text).as(JSON)
    }
  }

  def create = IsAuthenticated(parse.json){ username => implicit request =>
    //logger.info("in DatabaseController.create...")
    println("in DatabaseController.create...")
    try {
      val jsonObj = request.body.asInstanceOf[JsObject]
      val fileName = jsonObj.value.get("fileName").getOrElse(BadRequest("fileName not found")).toString()
      val sanitizedFileName = FileUtil.sanitizeFileName(fileName)
      // inject userId and documentType
      val fileExtension = FileUtil.fileExtension(sanitizedFileName)
      val fileType = FileType.withName(fileExtension.toUpperCase()) 
      val filePath = Configuration.uploadUserTempFilePath(userId, sanitizedFileName)
      val physicalName = TokenGenerator.token
      val signature = FileUtil.getMD5Hash(filePath)
      val documentObj = Json.obj("userId" -> userId) ++ Json.obj("documentType" -> DocumentType.TEXT) ++
                          Json.obj("fileType" -> fileType) ++ Json.obj("physicalName" -> physicalName) ++ 
                          Json.obj("signature" -> signature) ++ Json.obj("createdUserId" -> userId) ++ 
                          Json.obj("createdAt" -> new DateTime()) ++ jsonObj
      documentObj.validate[Document].fold(
              valid = { document =>
                      // rename the filename
                      val newFilePath = Configuration.uploadFilePath(physicalName)
                      FileUtil.move(filePath, newFilePath)
                      val documentId = DocumentRepository.create(document)
                      
                      // add DocumentUser entry
                      val userDocument = UserDocument(userId, documentId, OwnershipType.OWNED, true, true, true, false, userId)
                      UserDocumentRepository.create(userDocument)
                      
                      // find the saved document
                      val savedDocument = DocumentRepository.find(documentId)
                      // add the document in the lucene index
                      savedDocument match {
                        case Some(doc) => indexWriterActor ! MessageAddDocument(doc)
                        case None =>
                      }
                      // create a sharedDocument
                      
                      Ok(HttpResponseUtil.success()).as(JSON)
              },
              invalid = {
                  errors => BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
              }
        )
   } catch {
     case error: Throwable => BadRequest(HttpResponseUtil.error("Something is wrong, please try again!")).as(JSON)
   }
  }

  def share(documentId: Int) = IsAuthenticated(parse.json){ username => implicit request =>
    //logger.info(s"in DatabaseController.share(${documentId})")
    println(s"in DatabaseController.share(${documentId})")
    
    val jsonObj = request.body.asInstanceOf[JsObject]
    jsonObj.validate[Share].fold(
            valid = { share =>
                    val recipientUserIds = share.receivers.map(r => r.id)
                    MessageService.send(userId, None, share.subject, share.message, recipientUserIds)
                                                      
                    // create shared link
                    share.receivers.map(r => {
                                        val sharedDoc = UserDocument(r.id, documentId, OwnershipType.SHARED, share.canCopy, share.canShare, 
                                                      share.canView, share.isLimitedShare, userId, new DateTime(), share.shareUntilEOD)
                                        UserDocumentRepository.create(sharedDoc)
                                    })
                    Ok(HttpResponseUtil.success("Successfully shared!"))
            },
            invalid = {
                errors => BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
            }
      )
  }
  
  def updateShare(documentId: Int) = IsAuthenticated(parse.json){ username => implicit request =>
    //logger.info(s"in DatabaseController.updateShare(${documentId})")
    println(s"in DatabaseController.updateShare(${documentId})")
    
    val jsonObj = request.body.asInstanceOf[JsObject]
    jsonObj.validate[ContactWithDocument].fold(
            valid = { share =>
                    val optUserDocument = UserDocumentRepository.find(share.id, documentId)
                    optUserDocument match {
                      case Some(doc) =>
                          val userDocument = UserDocument(doc.userId, 
                                                      doc.documentId, 
                                                      doc.ownershipType, 
                                                      share.canCopy.getOrElse(doc.canCopy), 
                                                      share.canShare.getOrElse(doc.canShare), 
                                                      share.canView.getOrElse(doc.canView), 
                                                      share.isLimitedShare.getOrElse(doc.isLimitedShare), 
                                                      doc.createdUserId,
                                                      doc.createdAt,
                                                      share.shareUntilEOD,
                                                      Some(userId),
                                                      Some(new DateTime()))

                          UserDocumentRepository.udate(userDocument)
                          Ok(HttpResponseUtil.success("Successfully shared!"))
                      case None =>
                           BadRequest(HttpResponseUtil.error("Unable to find user document!"))
                    }
            },
            invalid = {
                errors => BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
            }
      )
  }
  
  def update(documentId: Int) = IsAuthenticated{ username => implicit request =>
    //logger.info("in DatabaseController.update...")
    println("in DatabaseController.update...")
    val json = request.body.asInstanceOf[JsObject]
    json.validate[Document].fold(
          valid = { Document =>
                  DocumentRepository.udate(Document)
                  Ok(HttpResponseUtil.success("Successfully updated!"))
          },
          invalid = {
              errors => BadRequest(HttpResponseUtil.error("Something is wrong, please try again!"))
          }
    )
  }

  def search(searchText: String) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DatabaseController.search(${searchText})")
    println(s"in DatabaseController.search(${searchText})")
    
    if (searchText.length() > 0){
      val documentIds = UserDocumentRepository.findDocumentIds(userId)
      implicit val timeout = Timeout(MESSAGE_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
      // send message to index searcher
      val f = ask(indexSearcherActor, MessageDocumentSearch(documentIds, searchText)).mapTo[MessageDocumentSearchResult]
      val result = f.map {
           case MessageDocumentSearchResult(docIds) => {
                  val userDocuments = UserDocumentRepository.findAllByDocumentIds(userId, docIds)
                  val text = Json.toJson(userDocuments)
                  Ok(text).as(JSON)
            }
           case _ => Ok("").as(JSON)
//                case Failure(failure) =>
//                        println(s"Failrure ${failure}")
//                        Ok("")
      }
      Await.result(result, timeout.duration)
    }
    else {
      Ok("")
    }
  }
  
  def searchDocument(documentId: Long, searchText: String) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DatabaseController.search(${documentId}, ${searchText})")
    println(s"in DatabaseController.search(${documentId}, ${searchText})")
    
    if (searchText.length() > 0){
      implicit val timeout = Timeout(MESSAGE_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
      // send message to index searcher
      val f = ask(indexSearcherActor, MessageSearchWithHighlighter(documentId, searchText)).mapTo[MessageSearchResultWithHighlighter]
      val result = f.map{
           case MessageSearchResultWithHighlighter(documentId, results) => {
                      val text = Json.toJson(results)
                      //println(text)
                      Ok(text).as(JSON)
            }
           case _ => Ok("").as(JSON)
//                case Failure(failure) =>
//                        println(s"Failrure ${failure}")
//                        Ok("")
      }
      
      Await.result(result, timeout.duration)
    }
    else {
      Ok("")
    }
  }
  
  def getContents(documentId: Long) = IsAuthenticated { username => implicit request =>
    //logger.info(s"in DatabaseController.getContents(${documentId})")
    println(s"in DatabaseController.getContents(${documentId})")
    
    implicit val timeout = Timeout(MESSAGE_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
    // send message to index searcher
    val f = ask(indexSearcherActor, MessageDocumentGetContents(documentId)).mapTo[MessageDocumentContents]
    val result = f.map{
         case MessageDocumentContents(documentId, contents) => {
                    val text = Json.toJson(contents)
                    println(text)
                    Ok(text).as(JSON)
          }
         case _ => Ok("").as(JSON)
//                case Failure(failure) =>
//                        println(s"Failrure ${failure}")
//                        Ok("")
    }
    
    Await.result(result, timeout.duration)
  }
  
  def delete(documentId: Long) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DatabaseController.delete(${documentId})")
    println(s"in DatabaseController.delete(${documentId})")
    // find document object from database
    val userDocument = UserDocumentRepository.find(userId, documentId)
    userDocument match {
      case Some(userDoc) =>
                    // delete all the tags
                    DocumentTagRepository.deleteByDocumentId(userId, documentId)
                    
                    // delete the database entry
                    UserDocumentRepository.delete(userId, documentId)
                    
                    Ok(HttpResponseUtil.success("Successfully deleted!"))
      case None => 
                    BadRequest(HttpResponseUtil.error("Unable to find document!"))
    }
  }
  
  def copy(documentId: Int) = IsAuthenticated { username => implicit request =>
    //logger.info(s"in DatabaseController.copy(${documentId})")
    println(s"in DatabaseController.copy(${documentId})")
    
    val document = DocumentRepository.find(documentId)
    // copy the document
    document match {
      case Some(doc) =>
            val sourceFilePath = Configuration.uploadFilePath(doc.physicalName)
            val physicalName = TokenGenerator.token
            val targetFilePath = Configuration.uploadFilePath(physicalName)
            FileUtil.copy(sourceFilePath, targetFilePath)
            val orgFileName = FileUtil.getOriginalName(doc.fileName)
            val orgName = FileUtil.getOriginalName(doc.name)
            val copyFileName = UserDocumentRepository.getCopyFileName(userId, orgFileName)
            val copyName = UserDocumentRepository.getCopyName(userId, orgName)
            
            val copyDocument = Document(None,
                                      userId,
                                      copyName,
                                      doc.documentType,
                                      doc.fileType,
                                      copyFileName,
                                      physicalName,
                                      doc.description,
                                      doc.signature,
                                      userId)
            val docId = DocumentRepository.create(copyDocument)
            
            // add DocumentUser entry
            val userDocument = UserDocument(userId, docId, OwnershipType.OWNED, true, true, true, false, userId)
            UserDocumentRepository.create(userDocument)
                      
            // find the saved document
            val savedDocument = DocumentRepository.find(docId)
            // add the document in the lucene index
            savedDocument match {
              case Some(doc) => 
                   indexWriterActor ! MessageAddDocument(doc)
                   Ok(HttpResponseUtil.success("Successfully copied the document"))
              case None =>
                    Ok(HttpResponseUtil.error("Unable to copy the document"))
            }
      case None =>
            Ok(HttpResponseUtil.error("Unable to find the document"))
    }
  }
}