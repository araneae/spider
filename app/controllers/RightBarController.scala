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

object RightBarController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def getCotents = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in RightBarController.getCotents()")
    println(s"in RightBarController.getCotents()")
    
    val optCompany = CompanyRepository.findByUserId(userId)
    optCompany match {
        case Some(company) =>
                Ok("").as(HTML)
        case None => 
              val content = views.html.rightBar()
              Ok(content).as(HTML)
      }
  }

}