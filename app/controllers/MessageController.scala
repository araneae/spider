package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import models.dtos.UserTag
import models.repositories.UserMessageRepository
import models.repositories.DocumentTagRepository
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Controller
import traits.Secured
import utils.HttpResponseUtil
import models.dtos._
import models.repositories._ 
import services._

object MessageController extends Controller with Secured {
  
  private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def get(messageId: Int) = IsAuthenticated{ username => implicit request =>
    logger.info("in MessageController.get(${messageId})")
    println("in MessageController.get(${messageId})")
    Ok("")
  }
  
  def getAll = IsAuthenticated{ username => implicit request =>
    logger.info("in MessageController.getAll()")
    println("in MessageController.getAll()")
    
    var list = UserMessageRepository.findAll(userId)
    val text = Json.toJson(list)
    Ok(text).as(JSON)
  }

  def create = IsAuthenticated(parse.json){ username => implicit request =>
      logger.info("in MessageController.create()")
      println("in MessageController.create()")
      val jsonObj = request.body.asInstanceOf[JsObject]
      val optSubject = (jsonObj \ "subject").asOpt[String]
      val optBody = (jsonObj \ "body").asOpt[String]
      val optReceivers = (jsonObj \ "receivers").asOpt[List[Connection]]
      
      optSubject match {
        case Some(subject) =>
            optBody match {
              case Some(body) =>
                  optReceivers match {
                    case Some(receivers) =>
                          MessageService.send(userId, None, subject, body, receivers.map(r => r.id))
                          Ok(HttpResponseUtil.success("Successfully sent message!"))
                    case None =>
                          Ok(HttpResponseUtil.error("Not found receivers list!"))
                  }
              case None =>
                  Ok(HttpResponseUtil.error("Not found message body!"))
            }
        case None =>
            Ok(HttpResponseUtil.error("Not found message subject!"))
      }
  }

  def delete(messageId: Int) = IsAuthenticated{ username => implicit request =>
    logger.info(s"in MessageController.delete(${messageId})")
    println(s"in MessageController.delete(${messageId})")
    
    UserMessageRepository.delete(messageId, userId)
    
    Ok(HttpResponseUtil.success("Successfully deleted!"))
  }
  
  def markStar(messageId: Int) = IsAuthenticated{ username => implicit request =>
    logger.info(s"in MessageController.markStar(${messageId})")
    println(s"in MessageController.markStar(${messageId})")
    
    UserMessageRepository.markStar(messageId, userId)
    Ok(HttpResponseUtil.success("Successfully marked star!"))
  }

  def removeStar(messageId: Int) = IsAuthenticated{ username => implicit request =>
    logger.info(s"in MessageController.removeStar(${messageId})")
    println(s"in MessageController.removeStar(${messageId})")
    
    UserMessageRepository.removeStar(messageId, userId)
    Ok(HttpResponseUtil.success("Successfully remove star!"))
  }

  def markImportant(messageId: Int) = IsAuthenticated{ username => implicit request =>
    logger.info(s"in MessageController.markImportant(${messageId})")
    println(s"in MessageController.markImportant(${messageId})")
    
    UserMessageRepository.markImportant(messageId, userId)
    Ok(HttpResponseUtil.success("Successfully marked important!"))
  }

  def removeImportant(messageId: Int) = IsAuthenticated{ username => implicit request =>
    logger.info(s"in MessageController.removeImportant(${messageId})")
    println(s"in MessageController.removeImportant(${messageId})")
    
    UserMessageRepository.removeImportant(messageId, userId)
    Ok(HttpResponseUtil.success("Successfully removed important!"))
  }
  
  def markRead(messageId: Int) = IsAuthenticated{ username => implicit request =>
    logger.info(s"in MessageController.markRead(${messageId})")
    println(s"in MessageController.markRead(${messageId})")
    
    UserMessageRepository.markRead(messageId, userId)
    Ok(HttpResponseUtil.success("Successfully marked read!"))
  }
  
  def reply(messageId: Int) = IsAuthenticated(parse.json){ username => implicit request =>
    logger.info(s"in MessageController.reply(${messageId})")
    println(s"in MessageController.reply(${messageId})")
    
    val jsonObj = request.body.asInstanceOf[JsObject]
    val optBody = (jsonObj \ "body").asOpt[String]
    optBody match {
      case Some(body) =>
            val optMessage = MessageRepository.find(messageId)
            optMessage match {
                case Some(orgMessage) =>
                      // create the reply message
                      MessageService.send(userId, orgMessage.messageId, orgMessage.subject, body, List(orgMessage.senderUserId))
                      Ok(HttpResponseUtil.success("Successfully replied message!"))
                case None =>  
                      Ok(HttpResponseUtil.success("Not found message!"))
            }
      case None =>
            Ok(HttpResponseUtil.success("Unable to parse payload!"))
    }
  }
  
  def trash(messageId: Int) = IsAuthenticated{ username => implicit request =>
    logger.info(s"in MessageController.trash(${messageId})")
    println(s"in MessageController.trash(${messageId})")
    val optTrash = MessageBoxRepository.findTrash(userId)
    optTrash match {
           case Some(trash) =>
              UserMessageRepository.moveMessageBox(messageId, trash.messageBoxId.get, userId)
              Ok(HttpResponseUtil.success("Successfully trashed message!"))
           case None =>
              Ok(HttpResponseUtil.success("Unable to find trash!"))
    }
  }
  
  def move(messageId: Int, messageBoxId: Int) = IsAuthenticated{ username => implicit request =>
    logger.info(s"in MessageController.move(${messageId}, ${messageBoxId})")
    println(s"in MessageController.move(${messageId}, ${messageBoxId})")
    UserMessageRepository.moveMessageBox(messageId, messageBoxId, userId)
    Ok(HttpResponseUtil.success("Successfully moved message!"))
  }
}