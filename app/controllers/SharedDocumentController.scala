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

object SharedDocumentController extends Controller with Secured with AkkaActor {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def getAll = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in SharedDocumentController.getAllByUserTagId(${userTagId})")
    println(s"in SharedDocumentController.getAll()")
    
    val list = UserDocumentBoxRepository.findSharedDocuments(userId)
    val text = Json.toJson(list)
    Ok(text).as(JSON)
  }
  
  def get(documentId: Long) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in SharedDocumentController.get(${documentId})")
    println(s"in SharedDocumentController.get(${documentId})")
    
    val document = DocumentRepository.find(documentId)
    document match {
        case Some(doc) => val json = Json.toJson(doc)
                          Ok(json).as(JSON)
        case None => BadRequest(HttpResponseUtil.error("Unable to find document!"))
    }
  }
  
  def getContents(documentId: Long) = IsAuthenticated { username => implicit request =>
    //logger.info(s"in SharedDocumentController.getContents(${documentId})")
    println(s"in SharedDocumentController.getContents(${documentId})")
    
    implicit val timeout = Timeout(MESSAGE_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
    // send message to index searcher
    val f = ask(indexSearcherActor, MessageDocumentGetContents(documentId)).mapTo[MessageDocumentContents]
    val result = f.map{
         case MessageDocumentContents(documentId, contents) => {
                    val text = Json.toJson(contents)
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
  
    def searchDocument(documentId: Long) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in SharedDocumentController.search(${documentId})")
    println(s"in SharedDocumentController.search(${documentId})")
    
    val optUserProfile = UserRepository.findUserProfilePersonal(userId)
    optUserProfile match {
      case Some(userProfile) =>
        val searchText = userProfile.xrayTerms
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
          Ok(HttpResponseUtil.reponseEmptyObject())
        }
      case None =>
        Ok(HttpResponseUtil.reponseEmptyObject())
    }
  }
    
    def copy(documentId: Long) = IsAuthenticated { username => implicit request =>
    //logger.info(s"in SharedDocumentController.copy(${documentId})")
    println(s"in SharedDocumentController.copy(${documentId})")
    
    val document = DocumentRepository.find(documentId)
    // copy the document
    document match {
      case Some(doc) =>
          val optDocumentBox = UserDocumentBoxRepository.findDefault(userId)
          optDocumentBox match {
            case Some(documentBox) =>
                val sourceFilePath = Configuration.uploadFilePath(doc.physicalName)
                val physicalName = TokenGenerator.token
                val targetFilePath = Configuration.uploadFilePath(physicalName)
                FileUtil.copy(sourceFilePath, targetFilePath)
                val orgFileName = FileUtil.getOriginalName(doc.fileName)
                val orgName = FileUtil.getOriginalName(doc.name)
                val copyFileName = UserDocumentRepository.getCopyFileName(userId, orgFileName)
                val copyName = UserDocumentRepository.getCopyName(userId, orgName)
                
                val copyDocument = Document(None,
                                          documentBox.documentBoxId,
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
                val userDocument = UserDocument(None, userId, docId, OwnershipType.OWNED, true, true, true, false, false, false, userId)
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