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
import enums.OwnershipType._
import models.dtos._
import models.repositories._
import services._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.JsObject
import play.api.libs.json.JsArray
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
  
  def getShareContacts(documentId: Long) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DatabaseController.getShareContacts(${documentId})")
    println(s"in DatabaseController.getShareContacts(${documentId})")
    
    val list = ContactRepository.findAllWithDocumentShareAttributes(userId, documentId)
    val data = Json.toJson(list)
    Ok(data).as(JSON)
  }
  
  def download(documentId: Long) = IsAuthenticated{ username => implicit request =>
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
  
  def getAllByUserTagId(userTagId: Long) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DatabaseController.getAllByUserTagId(${userTagId})")
    println(s"in DatabaseController.getAllByUserTagId(${userTagId})")
    
    if (userTagId > 0) {
      val list = DocumentTagRepository.findDocumentByUserTagId(userId, userTagId)
      val data = Json.toJson(list)
                  Ok(data).as(JSON)
    } else {  
      val list = UserDocumentRepository.getAll(userId)
      val data = Json.toJson(list)
      Ok(data).as(JSON)
    }
  }
  
  def getAllByDocumentFolderId(documentFolderId: Long) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DatabaseController.getAllByDocumentFolderId(${documentFolderId})")
    println(s"in DatabaseController.getAllByDocumentFolderId(${documentFolderId})")
    
    val list = UserDocumentFolderRepository.getAllDocumentsByDocumentFolderId(userId, documentFolderId)
    val data = Json.toJson(list)
    Ok(data).as(JSON)
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
      val documentObj = Json.obj("userId" -> userId) ++ 
                        Json.obj("documentType" -> DocumentType.TEXT) ++
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
                      val userDocument = UserDocument(None, userId, documentId, OwnershipType.OWNED, false, true, true, false, false, false, None, userId)
                      UserDocumentRepository.create(userDocument)
                      
                      // find the saved document
                      val savedDocument = DocumentRepository.find(documentId)
                      // add the document in the lucene index
                      savedDocument match {
                      case Some(doc) => indexWriterActor ! MessageAddDocument(doc)
                      case None =>
                      }
                      Ok(HttpResponseUtil.success()).as(JSON)
              },
              invalid = {
                  errors =>
                    BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
              }
      )
   } catch {
     case error: Throwable => BadRequest(HttpResponseUtil.error("Something is wrong, please try again!")).as(JSON)
   }
  }

  def share(documentId: Long) = IsAuthenticated(parse.json){ username => implicit request =>
    //logger.info(s"in DatabaseController.share(${documentId})")
    println(s"in DatabaseController.share(${documentId})")
    
    val jsonObj = request.body.asInstanceOf[JsObject]
    jsonObj.validate[ShareDTO].fold(
            valid = { share =>
                    val recipientUserIds = share.receivers.map(r => r.id)
                    MessageService.send(userId, None, share.subject, share.message, recipientUserIds)
                                                      
                    // create shared link
                    share.receivers.map(r => {
                                        val sharedDoc = UserDocument(None, r.id, documentId, OwnershipType.SHARED, share.canCopy, share.canShare, 
                                                  share.canView, false, false, share.isLimitedShare, share.shareUntilEOD, userId, new DateTime())
                                        UserDocumentRepository.create(sharedDoc)
                                    })
                    Ok(HttpResponseUtil.success("Successfully shared!"))
            },
            invalid = {
                errors => BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
            }
      )
  }
  
  def updateShare(documentId: Long) = IsAuthenticated(parse.json){ username => implicit request =>
    //logger.info(s"in DatabaseController.updateShare(${documentId})")
    println(s"in DatabaseController.updateShare(${documentId})")
    
    val jsonObj = request.body.asInstanceOf[JsObject]
    jsonObj.validate[ContactWithDocument].fold(
            valid = { share =>
                    println(share)
                    val optUserDocument = UserDocumentRepository.find(share.id, documentId)
                    optUserDocument match {
                      case Some(doc) =>
                          val userDocument = UserDocument(doc.userDocumentId,
                                                      doc.userId, 
                                                      doc.documentId, 
                                                      doc.ownershipType, 
                                                      share.canCopy.getOrElse(doc.canCopy), 
                                                      share.canShare.getOrElse(doc.canShare), 
                                                      share.canView.getOrElse(doc.canView), 
                                                      false,
                                                      false,
                                                      share.isLimitedShare.getOrElse(doc.isLimitedShare), 
                                                      share.shareUntilEOD,
                                                      doc.createdUserId,
                                                      doc.createdAt,
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
  
  def update(documentId: Long) = IsAuthenticated{ username => implicit request =>
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

  def search(userTagId: Long, searchText: String) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DatabaseController.search(${userTagId}, ${searchText})")
    println(s"in DatabaseController.search(${userTagId}, ${searchText})")
    
    if (searchText.length() > 0){
      val documentFolders = UserDocumentFolderRepository.findAll(userId)
      implicit val timeout = Timeout(MESSAGE_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
      // send message to index searcher
      val f = ask(indexSearcherActor, MessageDocumentSearch(documentFolders.map(b => b.documentFolderId), searchText)).mapTo[MessageDocumentSearchResult]
      val result = f.map {
           case MessageDocumentSearchResult(docIds) => {
                  val userDocuments = if (userTagId > 0) {
                                          DocumentTagRepository.findDocumentByUserTagIdAndDocumentIds(userId, userTagId, docIds)
                                      } else {
                                          UserDocumentRepository.findAllByDocumentIds(userId, docIds)
                                      }
                  val data = Json.toJson(userDocuments)
                  Ok(data).as(JSON)
            }
           case _ => Ok("").as(JSON)
//                case Failure(failure) =>
//                        println(s"Failrure ${failure}")
//                        Ok("")
      }
      Await.result(result, timeout.duration)
    }
    else {
      Ok(HttpResponseUtil.reponseEmptyObject())
    }
  }
  
  def searchDocument(documentId: Long) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DatabaseController.searchDocument(${documentId})")
    println(s"in DatabaseController.searchDocument(${documentId})")
    
    val optUserProfile = UserRepository.findUserProfilePersonal(userId)
    optUserProfile match {
      case Some((user, userProfile)) =>
        val searchText = userProfile.xrayTerms
        if (searchText.length() > 0){
          implicit val timeout = Timeout(MESSAGE_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
          // send message to index searcher
          val f = ask(indexSearcherActor, MessageSearchWithHighlighter(documentId, searchText)).mapTo[MessageSearchResultWithHighlighter]
          val result = f.map{
               case MessageSearchResultWithHighlighter(documentId, results) => {
                          val data = Json.toJson(results)
                          //println(data)
                          Ok(data).as(JSON)
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
      case None =>
        Ok(HttpResponseUtil.reponseEmptyObject())
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
                    val data = Json.toJson(contents)
                    //println(data)
                    Ok(data).as(JSON)
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
    val userDocuments = UserDocumentRepository.findAllByDocumentId(documentId)
    val sharedDocuments = userDocuments.filter { x => x.ownershipType == OwnershipType.SHARED }
    if (sharedDocuments.length > 0) {
      BadRequest(HttpResponseUtil.error("Unable to delete the document, the document has been shared with one or more contacts!"))
    }
    else {
      val attachments = JobApplicationAttachmentRepository.findByDocumentId(documentId)
      if (attachments.length > 0) {
        BadRequest(HttpResponseUtil.error("Unable to delete the document, the document has been attached in one or more job applications!"))
      }
      else {
        val userDocument = UserDocumentRepository.find(userId, documentId)
        userDocument match {
          case Some(userDoc) =>
                    // delete all the tags
                    DocumentTagRepository.deleteByDocumentId(userId, documentId)
                    
                    // delete the database entry
                    UserDocumentRepository.delete(userId, documentId)
                    
                    // delete the document
                    DocumentRepository.delete(documentId)
                    
                    // delete from index
                    indexWriterActor ! MessageDeleteDocument(documentId)
                    
                    Ok(HttpResponseUtil.success("Successfully deleted!"))
          case None => 
                    BadRequest(HttpResponseUtil.error("Unable to find document!"))
        }
      }
    }
  }
  
  def moveToFolder(documentId: Long, documentFolderId: Long) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DatabaseController.moveToFolder(${documentId}, ${documentFolderId})")
    println(s"in DatabaseController.moveToFolder(${documentId}, ${documentFolderId})")
    
    val optDocument = DocumentRepository.find(documentId)
    optDocument match {
      case Some(document) =>
          DocumentRepository.udateFolder(documentId, documentFolderId, userId)
          Ok(HttpResponseUtil.success("Successfully shared!"))
      case None =>
          BadRequest(HttpResponseUtil.error("Unable to update document!"))
    }
  }
  
  def copy(documentId: Long) = IsAuthenticated { username => implicit request =>
    //logger.info(s"in DatabaseController.copy(${documentId})")
    println(s"in DatabaseController.copy(${documentId})")
    
    val document = DocumentRepository.find(documentId)
    // copy the document
    document match {
      case Some(doc) =>
          val optDocumentFolder = UserDocumentFolderRepository.findDefault(userId)
          optDocumentFolder match {
            case Some(documentFolder) =>
                val sourceFilePath = Configuration.uploadFilePath(doc.physicalName)
                val physicalName = TokenGenerator.token
                val targetFilePath = Configuration.uploadFilePath(physicalName)
                FileUtil.copy(sourceFilePath, targetFilePath)
                val orgFileName = FileUtil.getOriginalName(doc.fileName)
                val orgName = FileUtil.getOriginalName(doc.name)
                val copyFileName = UserDocumentRepository.getCopyFileName(userId, orgFileName)
                val copyName = UserDocumentRepository.getCopyName(userId, orgName)
                
                val copyDocument = Document(None,
                                          documentFolder.documentFolderId,
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
                val userDocument = UserDocument(None, userId, docId, OwnershipType.OWNED, false, true, true, false, false, false, None, userId)
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
              Ok(HttpResponseUtil.error("Unable to find the document store"))
          }
      case None =>
            Ok(HttpResponseUtil.error("Unable to find the document"))
    }
  }
}