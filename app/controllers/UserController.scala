package controllers

import org.mindrot.jbcrypt.BCrypt
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import play.api.mvc.MultipartFormData
import play.api.mvc.MultipartFormData.FilePart
import play.api.libs.Files.TemporaryFile
import java.io.File
import play.api.data.Form
import play.api.data.Forms.nonEmptyText
import play.api.data.Forms.tuple
import org.joda.time.DateTime
import play.api.db.slick._
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.mvc.Security
import play.api.libs.json._
import models.repositories._
import traits._
import utils._
import models.dtos._
import services._

object UserController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def get = IsAuthenticated{ username => implicit request =>
      //logger.info("in UserController.get")
      println("in UserController.get")
      var optUser = UserRepository.find(userId)
      optUser match {
        case Some(user) =>
            val userDTO = UserDTO(user)
            val data = Json.toJson(userDTO)
            Ok(data).as(JSON)
        case None => 
          // currently logged-in user is not found -- error
          BadRequest(HttpResponseUtil.error("Unable to find user!"))
      }
  }
  
}