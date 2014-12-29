package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import models.dtos._
import models.repositories._
import play.api.libs.json.JsObject
import play.api.libs.json.JsArray
import play.api.libs.json.Json
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import java.util.concurrent.TimeUnit
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Controller
import traits.Secured
import utils.HttpResponseUtil
import org.joda.time.DateTime
import enums._
import traits._
import models.dtos._
import actors._

object DocumentFolderController extends Controller with Secured  with AkkaActor {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def get(documentFolderId: Long) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DocumentFolderController.get(${documentFolderId})")
    println(s"in DocumentFolderController.get(${documentFolderId})")
    
    val optDocumentFolderDTO = DocumentFolderRepository.get(documentFolderId)
    val data = Json.toJson(optDocumentFolderDTO)
    Ok(data).as(JSON)
  }
  
  def getAll = IsAuthenticated{ username => implicit request =>
    //logger.info("in DocumentFolderController.getAll()")
    println("in DocumentFolderController.getAll()")
    
    var list = UserDocumentFolderRepository.getAll(userId)
    val data = Json.toJson(list)
    Ok(data).as(JSON)
  }

  def create = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in DocumentFolderController.create()")
      println("in DocumentFolderController.create()")
      val jsonObj = request.body.asInstanceOf[JsObject]
      jsonObj.validate[FolderDTO].fold(
            valid = { folderDTO =>
                    val documentFolder = DocumentFolder(folderDTO, userId, new DateTime, None, None)
                    val documentFolderId = DocumentFolderRepository.create(documentFolder)
                    val userDocumentFolder = UserDocumentFolder(None, documentFolderId, userId, OwnershipType.OWNED, false, true, true, false, None, userId)
                    UserDocumentFolderRepository.create(userDocumentFolder)
                    Ok(HttpResponseUtil.success("Successfully created folder!"))
            },
            invalid = {
                errors => BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
            }
      )
  }
  
  def getFolderShareContacts(documentFolderId: Long) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DocumentFolderController.getFolderShareContacts(${documentFolderId})")
    println(s"in DocumentFolderController.getFolderShareContacts(${documentFolderId})")
    val list = ContactRepository.findAllWithDocumentFolderShareAttributes(userId, documentFolderId)
    val data = Json.toJson(list)
    Ok(data).as(JSON)
  }
  
  def update(documentFolderId: Long) = IsAuthenticated(parse.json){ username => implicit request =>
    //logger.info(s"in DocumentFolderController.update(${documentFolderId})")
    println(s"in DocumentFolderController.update(${documentFolderId})")
    val json = request.body.asInstanceOf[JsObject]
    json.validate[FolderDTO].fold(
          valid = { folderDTO =>
                  val optDocumentFolder = DocumentFolderRepository.find(documentFolderId)
                  optDocumentFolder match {
                    case Some(documentFolder) =>
                        val updatedDocumentFolder = DocumentFolder(folderDTO, 
                                                                   documentFolder.createdUserId, 
                                                                   documentFolder.createdAt,
                                                                   Some(userId),
                                                                   Some(new DateTime))
                        DocumentFolderRepository.udate(updatedDocumentFolder)
                        Ok(HttpResponseUtil.success("Successfully updated!"))
                    case None =>
                        BadRequest(HttpResponseUtil.error("Folder not found!"))
                  }
          },
          invalid = {
              errors => println(errors)
                BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
          }
    )
  }
  
  def delete(documentFolderId: Long) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DocumentFolderController.delete(${documentFolderId})")
    println(s"in DocumentFolderController.delete(${documentFolderId})")
    
    // first delete if the folder is empty
    val documentCount = DocumentRepository.getDocumentCount(documentFolderId)
    if (documentCount > 0) {
        UserDocumentFolderRepository.delete(documentFolderId)
        DocumentFolderRepository.delete(documentFolderId)
        Ok(HttpResponseUtil.success("Successfully deleted folder!"))
    }
    else {
        BadRequest(HttpResponseUtil.error("Folder is not empty!"))
    }
  }
  
  def shareFolder(documentFolderId: Long) = IsAuthenticated(parse.json){ username => implicit request =>
    //logger.info(s"in DocumentFolderController.shareFolder(${documentFolderId})")
    println(s"in DocumentFolderController.shareFolder(${documentFolderId})")
    
    val jsonObj = request.body.asInstanceOf[JsArray]
    jsonObj.validate[List[ContactWithDocumentFolder]].fold(
            valid = { shares =>
                        shares.map { share =>
                          val optUserDocumentFolder = UserDocumentFolderRepository.find(share.id, documentFolderId)
                          optUserDocumentFolder match {
                            case Some(doc) =>
                                if (share.shared) {
                                  val userDocument = UserDocumentFolder(
                                                            doc.userDocumentFolderId,
                                                            doc.documentFolderId, 
                                                            doc.userId, 
                                                            doc.ownershipType, 
                                                            share.canCopy.getOrElse(doc.canCopy),
                                                            true,
                                                            true,
                                                            share.isLimitedShare.getOrElse(doc.isLimitedShare), 
                                                            share.shareUntilEOD,
                                                            doc.createdUserId,
                                                            doc.createdAt,
                                                            Some(userId),
                                                            Some(new DateTime()))
      
                                  UserDocumentFolderRepository.udate(userDocument)
                                }
                                else {
                                  UserDocumentFolderRepository.delete(doc.userDocumentFolderId.get)
                                }
                            case None =>
                                 if (share.shared) {
                                   val userDocument = UserDocumentFolder(
                                                              None,
                                                              documentFolderId, 
                                                              share.id, 
                                                              OwnershipType.SHARED, 
                                                              share.canCopy.getOrElse(false),
                                                              true,
                                                              true,
                                                              share.isLimitedShare.getOrElse(false), 
                                                              share.shareUntilEOD,
                                                              userId)
        
                                   UserDocumentFolderRepository.create(userDocument)
                                 }
                          }
                        }
                        Ok(HttpResponseUtil.success("Successfully shared!"))
            },
            invalid = {
                errors => BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
            }
      )
  }
  
  def searchInFolder(documentFolderId: Long, searchText: String) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DatabaseController.searchInFolder(${documentFolderId}, ${searchText})")
    println(s"in DatabaseController.searchInFolder(${documentFolderId}, ${searchText})")
    
    if (searchText.length() > 0) {
      implicit val timeout = Timeout(MESSAGE_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
      // send message to index searcher
      val f = ask(indexSearcherActor, MessageDocumentSearch(List(documentFolderId), searchText)).mapTo[MessageDocumentSearchResult]
      val result = f.map {
           case MessageDocumentSearchResult(docIds) => {
                  val userDocuments = UserDocumentFolderRepository.getDocumentsByDocumentIds(userId, docIds)
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
  
  def search(searchText: String) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DatabaseController.search(${searchText})")
    println(s"in DatabaseController.search(${searchText})")
    
    if (searchText.length() > 0) {
      val documentFolders = UserDocumentFolderRepository.findAll(userId)
      implicit val timeout = Timeout(MESSAGE_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
      // send message to index searcher
      val f = ask(indexSearcherActor, MessageDocumentSearch(documentFolders.map{f => f.documentFolderId}, searchText)).mapTo[MessageDocumentSearchResult]
      val result = f.map {
           case MessageDocumentSearchResult(docIds) => {
                  val userDocuments = UserDocumentFolderRepository.getDocumentsByDocumentIds(userId, docIds)
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
}