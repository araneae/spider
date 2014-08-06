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
import play.api.mvc.AsyncResult
import scala.util.Success
import scala.util.Failure
import play.api.libs.concurrent.Execution.Implicits._
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException

object ConnectionController extends Controller with Secured {
  
  private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  private final val DEFAULT_SUBJECT = "Shared a document"
  
  // create one lucene actor
  val MESSAGE_TIMEOUT_IN_MILLIS = 5000
  val indexWriterActor = Akka.system.actorOf(Props[IndexWriterActor])
  
  def getInNetwrokConnections = IsAuthenticated{ username => implicit request =>
    logger.info(s"in ConnectionController.getInNetwrokConnections()")
    println(s"in ConnectionController.getInNetwrokConnections()")
    
    val list = ConnectionRepository.findAll(userId)
    val text = Json.toJson(list)
    Ok(text).as(JSON)
  }
  
  def shareInNetwrokConnections(documentId: Int) = IsAuthenticated(parse.json){ username => implicit request =>
    logger.info(s"in ConnectionController.shareInNetwrokConnections(${documentId})")
    println(s"in ConnectionController.shareInNetwrokConnections(${documentId})")
    
    val jsonObj = request.body.asInstanceOf[JsObject]
    jsonObj.validate[Share].fold(
            valid = { share =>
                    // create a message
                    val message = Message(None, None, userId, false, DEFAULT_SUBJECT, Some(share.message))
                    val messageId = MessageRepository.create(message)
                    
                    // iterate through the recipients
                    share.receivers.map{ connection =>
                                          // send message to recipient
                                          // find recipient's inbox
                                          val inbox = MessageBoxRepository.findInbox(connection.id)
                                          inbox match {
                                            case Some(box) =>
                                                      // add the recipient
                                                      val recipient = MessageRecipient(connection.id, messageId, false)
                                                      MessageRecipientRepository.create(recipient)
                                                      
                                                      // add the message intorecipient's inbox
                                                      val userMessage = UserMessage(connection.id, 
                                                                                    messageId,
                                                                                    box.messageBoxId.get,
                                                                                    false,
                                                                                    false,
                                                                                    false)
                                                      UserMessageRepository.create(userMessage)
                                                      
                                                      // create shared link
                                                      val sharedDoc = SharedDocument(connection.id, documentId, userId, share.canEdit)
                                                      SharedDocumentRepository.create(sharedDoc)
//                                                      // copy the document
//                                                      // find the document
//                                                      val document = DocumentRepository.find(documentId)
//                                                      // copy the document
//                                                      document match {
//                                                        case Some(doc) =>
//                                                              val srcUploadPath = Configuration.uploadFilePath(userId, doc.physicalName)
//                                                              val physicalName = TokenGenerator.token
//                                                              val tgtFilePath = Configuration.uploadFilePath(connection.id, physicalName)
//                                                              FileUtil.createPath(Configuration.uploadPath(userId))
//                                                              FileUtil.copy(srcUploadPath, tgtFilePath)
//                                                              
//                                                              val newDocument = Document(None, 
//                                                                                        connection.id,
//                                                                                        doc.name,
//                                                                                        doc.documentType,
//                                                                                        doc.fileType,
//                                                                                        doc.fileName,
//                                                                                        physicalName,
//                                                                                        doc.name)
//                                                              val docId = DocumentRepository.create(newDocument)
//                                                              // find the saved document
//                                                              val savedDocument = DocumentRepository.find(docId)
//                                                              // add the document in the lucene index
//                                                              savedDocument match {
//                                                                case Some(doc) => indexWriterActor ! MessageAddDocument(userId, doc)
//                                                                case None =>
//                                                              }
//                                                        case None =>
//                                                      }
                                                      
                                            case None => 
                                                        // ignore the recipient
                                          }
                    }
                    
                    // add the message in the user's sentitems 
                    val sentItem = MessageBoxRepository.findSentItems(userId)
                    sentItem match {
                      case Some(box) => 
                                    // add the message intorecipient's inbox
                                    val userMessage = UserMessage(userId, 
                                                                  messageId,
                                                                  box.messageBoxId.get,
                                                                  true,
                                                                  false,
                                                                  false)
                                    UserMessageRepository.create(userMessage)
                      case None => 
                                    // error - ignore for now
                    }
                    Ok(HttpResponseUtil.success("Successfully shared!"))
            },
            invalid = {
                errors => BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
            }
      )
  }
}