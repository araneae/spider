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
import scala.util.Success
import scala.util.Failure
import play.api.libs.concurrent.Execution.Implicits._
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException

object SharedDatabaseController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def getAll = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in SharedDatabaseController.getAll()")
    println(s"in SharedDatabaseController.getAll()")
    
    val list = SharedDocumentRepository.findAll(userId)
    val text = Json.toJson(list)
    Ok(text).as(JSON)
  }
 
  def get(documentId: Long) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in SharedDatabaseController.get(${documentId})")
    println(s"in SharedDatabaseController.get(${documentId})")
    // find document object from database
    val document = SharedDocumentRepository.find(userId, documentId)
    document match {
      case Some(doc) =>
                    val json = Json.toJson(doc)
                    Ok(json).as(JSON)
      case None => 
                    Ok(HttpResponseUtil.reponseEmptyObject)
    }
  }
  
  def delete(documentId: Long) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in SharedDatabaseController.delete(${documentId})")
    println(s"in SharedDatabaseController.delete(${documentId})")
    // find document object from database
    val document = SharedDocumentRepository.find(userId, documentId)
    document match {
      case Some(doc) =>
                    // delete the database entry
                    SharedDocumentRepository.delete(userId, documentId)
                    
                    Ok(HttpResponseUtil.success("Successfully deleted!"))
      case None => 
                    BadRequest(HttpResponseUtil.error("Unable to find document!"))
    }
  }
}