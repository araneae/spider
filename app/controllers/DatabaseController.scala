package controllers

import java.io.File

import org.joda.time.DateTime
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import actors._
import akka.pattern.ask
import akka.util.Timeout
import java.util.concurrent.TimeUnit
import enums.DocumentType
import enums.FileType
import enums.OwnershipType
import models.dtos.Document
import models.dtos.UserDocument
import models.repositories._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Controller
import scala.concurrent.Await
import traits.AkkaActor
import traits.Secured
import utils.Configuration
import utils.FileUtil
import utils.HttpResponseUtil
import utils.TokenGenerator

object DatabaseController extends Controller with Secured with AkkaActor {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def upload = IsAuthenticated{ username => implicit request =>
      //logger.info("in DatabaseController.upload...")
      println("in DatabaseController.upload...")
      val body = request.body.asMultipartFormData
      body match { 
        case Some(maltiPartData) =>
            val filePart = maltiPartData.file("resume")
            filePart match { 
              case Some(file) =>
                    val uploadPath = Configuration.uploadUserTempFilePath(userId, file.filename)
                    // check if the file aready exists
                    //val existingDocument = DocumentRepository.findDocument(userId, file.filename)
                    //existingDocument match {
                    //  case Some(doc) => BadRequest(HttpResponseUtil.error("File Aready Exists!"))
                    //  case None => file.ref.moveTo(new File(uploadPath), true)
                    //               Ok(HttpResponseUtil.success("Successfully Uploaded!"))
                    //}
                    file.ref.moveTo(new File(uploadPath), true)
                    Ok(HttpResponseUtil.success("Successfully Uploaded!"))
              case None => BadRequest(HttpResponseUtil.error("Unable to upload file, please try again!"))
            }
       case None => BadRequest(HttpResponseUtil.error("Unable to upload file, please try again!"))
      }
  }
  
  def getAllConnections(documentId: Int) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DatabaseController.getAllConnections(${documentId})")
    println(s"in DatabaseController.getAllConnections(${documentId})")
    
    val list = ContactRepository.findAllWithDocumentShareAttributes(userId, documentId)
    val text = Json.toJson(list)
    println(text)
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
                      val userDocument = UserDocument(userId, documentId, OwnershipType.OWNED, true, true, true, userId)
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
      //AsyncResult {
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
      //}
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
      //AsyncResult {
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
      //}
    }
    else {
      Ok("")
    }
  }
  
  def delete(documentId: Long) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DatabaseController.delete(${documentId})")
    println(s"in DatabaseController.delete(${documentId})")
    // find document object from database
    val document = DocumentRepository.find(documentId)
    document match {
      case Some(doc) =>
                    // delete the file from disk - DON'T delete until confirming sharing with other users
                    //val filePath = Configuration.uploadFilePath(userId, doc.physicalName)
                    //FileUtil.delete(filePath)
                    
                    //delete from the index
                    //indexWriterActor ! MessageDeleteDocument(userId, documentId)
                    
                    // delete all the tags
                    DocumentTagRepository.deleteByDocumentId(userId, documentId)
                    
                    // delete the database entry
                    DocumentRepository.delete(documentId)
                    
                    Ok(HttpResponseUtil.success("Successfully deleted!"))
      case None => 
                    BadRequest(HttpResponseUtil.error("Unable to find document!"))
    }
  }
}