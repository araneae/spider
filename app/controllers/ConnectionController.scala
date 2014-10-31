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
import play.api.libs.json._
import play.api.mvc.Controller
import play.api.mvc.MultipartFormData._
import traits._
import utils._
import services._
import org.joda.time.DateTime
import scala.util.Success
import scala.util.Failure

object ConnectionController extends Controller with Secured with AkkaActor {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def getAll = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in ConnectionController.getAll()")
    println(s"in ConnectionController.getAll()")
    
    val list = ConnectionRepository.findAll(userId)
    val text = Json.toJson(list)
    Ok(text).as(JSON)
  }
}