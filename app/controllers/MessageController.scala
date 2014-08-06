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
      
      Ok(HttpResponseUtil.success("Successfully created message!"))
  }

  def delete(messageId: Int) = IsAuthenticated{ username => implicit request =>
    logger.info(s"in MessageController.delete(${messageId})")
    println(s"in MessageController.delete(${messageId})")
    
    Ok(HttpResponseUtil.success("Successfully deleted!"))
  }
  
  def markStar(messageId: Int) = IsAuthenticated{ username => implicit request =>
    logger.info(s"in MessageController.markStar(${messageId})")
    println(s"in MessageController.markStar(${messageId})")
    
    UserMessageRepository.markStar(messageId)
    Ok(HttpResponseUtil.success("Successfully marked star!"))
  }

  def removeStar(messageId: Int) = IsAuthenticated{ username => implicit request =>
    logger.info(s"in MessageController.removeStar(${messageId})")
    println(s"in MessageController.removeStar(${messageId})")
    
    UserMessageRepository.removeStar(messageId)
    Ok(HttpResponseUtil.success("Successfully remove star!"))
  }

  def markImportant(messageId: Int) = IsAuthenticated{ username => implicit request =>
    logger.info(s"in MessageController.markImportant(${messageId})")
    println(s"in MessageController.markImportant(${messageId})")
    
    UserMessageRepository.markImportant(messageId)
    Ok(HttpResponseUtil.success("Successfully marked important!"))
  }

  def removeImportant(messageId: Int) = IsAuthenticated{ username => implicit request =>
    logger.info(s"in MessageController.removeImportant(${messageId})")
    println(s"in MessageController.removeImportant(${messageId})")
    
    UserMessageRepository.removeImportant(messageId)
    Ok(HttpResponseUtil.success("Successfully removed important!"))
  }
}