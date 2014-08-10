package controllers

import org.mindrot.jbcrypt.BCrypt
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import play.api.data.Form
import play.api.data.Forms.nonEmptyText
import play.api.data.Forms.tuple
import play.api.db.slick._
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.mvc.Security
import models.repositories.UserRepository
import traits._

object SettingsController extends Controller with Secured {
  
  private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def settings = IsAuthenticated{ username => implicit request =>
      logger.info("in SettingsController.settings...")
      println("in SettingsController.settings...")
      Ok(views.html.settings("Recruiter Tool"))
  }
}