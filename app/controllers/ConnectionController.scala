package controllers

import java.io.File
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import actors._
import actors.IndexWriterActor
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import enums._
import models.dtos._
import models.repositories._
import play.api.Play
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.json._
import play.api.mvc.Controller
import play.api.mvc.MultipartFormData._
import traits.Secured
import utils._
import services._
import play.api.mvc.AsyncResult
import scala.util.Success
import scala.util.Failure
import play.api.libs.concurrent.Execution.Implicits._

object ConnectionController extends Controller with Secured {
  
  private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  // create one lucene actor
  val MESSAGE_TIMEOUT_IN_MILLIS = 5000
  val indexWriterActor = Akka.system.actorOf(Props[IndexWriterActor])
  
  def getAll = IsAuthenticated{ username => implicit request =>
    logger.info(s"in ConnectionController.getAll()")
    println(s"in ConnectionController.getAll()")
    
    val list = ConnectionRepository.findAll(userId)
    val text = Json.toJson(list)
    Ok(text).as(JSON)
  }
  
  def share(documentId: Int) = IsAuthenticated(parse.json){ username => implicit request =>
    logger.info(s"in ConnectionController.share(${documentId})")
    println(s"in ConnectionController.share(${documentId})")
    
    val jsonObj = request.body.asInstanceOf[JsObject]
    jsonObj.validate[Share].fold(
            valid = { share =>
                    val recipientUserIds = share.receivers.map(r => r.id)
                    MessageService.send(userId, None, share.subject, share.message, recipientUserIds)
                                                      
                    // create shared link
                    share.receivers.map(r => {
                                        val sharedDoc = SharedDocument(r.id, documentId, userId, share.canCopy, share.canShare, userId)
                                        SharedDocumentRepository.create(sharedDoc)
                                    })
                    Ok(HttpResponseUtil.success("Successfully shared!"))
            },
            invalid = {
                errors => BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
            }
      )
  }
  
  def copy(documentId: Int) = IsAuthenticated{ username => implicit request =>
    logger.info(s"in ConnectionController.copy(${documentId})")
    println(s"in ConnectionController.copy(${documentId})")
    
    val document = DocumentRepository.find(documentId)
    val sharedDocument = SharedDocumentRepository.find(userId, documentId)
    // copy the document
    document match {
      case Some(doc) =>
            sharedDocument match {
              case Some(sharedDoc) =>
                  val sourceUploadPath = Configuration.uploadFilePath(sharedDoc.sharedUserId, doc.physicalName)
                  val physicalName = TokenGenerator.token
                  val trgetFilePath = Configuration.uploadFilePath(userId, physicalName)
                  FileUtil.createPath(Configuration.uploadPath(userId))
                  FileUtil.copy(sourceUploadPath, trgetFilePath)
                  
                  val newDocument = Document(None,
                                            userId,
                                            doc.name,
                                            doc.documentType,
                                            doc.fileType,
                                            doc.fileName,
                                            physicalName,
                                            doc.name,
                                            doc.signature,
                                            userId)
                  val docId = DocumentRepository.create(newDocument)
                  // find the saved document
                  val savedDocument = DocumentRepository.find(docId)
                  // add the document in the lucene index
                  savedDocument match {
                    case Some(doc) => 
                         indexWriterActor ! MessageAddDocument(userId, doc)
                         Ok(HttpResponseUtil.success("Successfully copied the shared document"))
                    case None =>
                          Ok(HttpResponseUtil.error("Unable to copy the shared document"))
                  }
              case None =>
                 Ok(HttpResponseUtil.error("Unable to find the shared document"))
            }
      case None =>
             Ok(HttpResponseUtil.error("Unable to find the document"))
    }
  }
}