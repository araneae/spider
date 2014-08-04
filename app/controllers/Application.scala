package controllers

import org.mindrot.jbcrypt.BCrypt
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import models.dtos.User
import play.api.data.Form
import play.api.data.Forms.nonEmptyText
import play.api.data.Forms.tuple
import play.api.db.slick._
import play.api.mvc.Action
import play.api.mvc.Controller
import models.repositories.UserRepository
import traits.Secured
import play.api.mvc.Flash
import models.repositories._

object Application extends Controller with Secured {
  
  private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  val signupForm = Form(
    tuple(
      "first_name" -> nonEmptyText,
      "last_name" -> nonEmptyText,
      "email" -> nonEmptyText,
      "password" -> nonEmptyText
    )
  )
  
  def index = Action {
    logger.info("Application.index...")
    Ok(views.html.index("Recruiter Tool"))
  }
  
  def home = IsAuthenticated { username => implicit request =>
    Ok(views.html.home(s"Welcome back ${name}"))
  }
  
  def signup = Action { implicit request =>
    logger.info("Application.signup...")
    val form = if (flash.get("error").isDefined) 
                  signupForm.bind(flash.data)
                else
                  signupForm
    Ok(views.html.signup(form))
  }
  
  def register = Action {
    implicit request => 
       logger.info("Application.register...") 
       val formData = signupForm.bindFromRequest
       formData.fold(
          hasErrors = { form => 
            BadRequest(views.html.index("Error"))
          },
          success = { value => 
            logger.info(s"register $value") 
            value match {
              case Tuple4(first_name, last_name, email, password) => {
                // find if the user already exists
                val existingUser = UserRepository findByEmail email
                existingUser match {
                  case Some (u) => {
                     Redirect(routes.Application.signup).flashing(Flash(formData.data) + ("error" -> "Email already exists"))
                  }
                  case None => {
                      val encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                      val user = User(None, first_name, last_name, email, encryptedPassword)
                      val userId = UserRepository create user
                      // create default message boxes
                      MessageBoxRepository.createDefaults(userId)
                      Redirect(routes.AuthController.login)
                  }
                }
              }
            }
          }
        )
  }
}