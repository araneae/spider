package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import models.dtos.UserTag
import models.repositories.MessageBoxRepository
import models.repositories.DocumentTagRepository
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Controller
import traits.Secured
import utils.HttpResponseUtil
import models.dtos._
import enums._

object MessageBoxController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def create = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in MessageBoxController.create()")
      println("in MessageBoxController.create()")
      val jsonObj = request.body.asInstanceOf[JsObject]
      val optName = (jsonObj \ "name").asOpt[String]
      
      optName match {
        case Some(name) =>
              val messageBox = MessageBox(None, userId, MessageBoxType.CUSTOM, name, userId)
              val messageBoxId = MessageBoxRepository.create(messageBox)
              Ok(HttpResponseUtil.success("Successfully created message box!"))
        case None =>
              Ok(HttpResponseUtil.error("Not found message box name"))
      }
  }
  
  def update(messageBoxId: Int) = IsAuthenticated{ username => implicit request =>
    //logger.info("in MessageBoxController.update(${messageBoxId})")
    println("in MessageBoxController.update(${messageBoxId})")
    
    var list = MessageBoxRepository.findAll(userId)
    val text = Json.toJson(list)
    Ok(text).as(JSON)
  }
  
  def rename(messageBoxId: Int) = IsAuthenticated(parse.json){ username => implicit request =>
    //logger.info(s"in MessageBoxController.update(${messageBoxId})")
    println(s"in MessageBoxController.update(${messageBoxId})")
    val jsonObj = request.body.asInstanceOf[JsObject]
    val optName = (jsonObj \ "name").asOpt[String]
    optName match {
      case Some(name) =>
        MessageBoxRepository.rename(messageBoxId, name, userId)
        Ok(HttpResponseUtil.success("Successfully renamed message box!"))
      case None =>  
        BadRequest(HttpResponseUtil.error("Unable to rename message box!"))
    }
  }
  
  def getAll = IsAuthenticated{ username => implicit request =>
    //logger.info("in MessageBoxController.getAll()")
    println("in MessageBoxController.getAll()")
    
    var list = MessageBoxRepository.findAll(userId)
    val text = Json.toJson(list)
    Ok(text).as(JSON)
  }

}