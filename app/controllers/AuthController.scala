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

object AuthController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  val loginForm = Form(
    tuple(
      "email" -> nonEmptyText,
      "password" -> nonEmptyText
    ) verifying ("Invalid email or password", result => result match {
      case (email, password) => check(email, password)
    })
  )

  def check(username: String, password: String) = {
    (username.length() > 0 && password.length() > 0)  
  }

  def login = Action { 
    implicit request =>
      Ok(views.html.login(loginForm))
  }

  def authenticate = Action { 
    implicit request =>
      var success = false
      var userName = ""
      var userId = ""
      var firstName = ""
      loginForm.bindFromRequest.fold(
        formWithErrors => {
          BadRequest(views.html.login(formWithErrors))
        },
        value => {
          value match {
            case Tuple2(email, password) => {
              //logger.info(s"$email $password")
              UserRepository.findByEmail(email).map { 
                user => {
                  if (BCrypt.checkpw(password, user.password)) {
                    userName = user.email
                    userId = user.userId.get.toString
                    firstName = user.firstName
                    success = true
                  }
                }
              }.getOrElse(
                Forbidden
              )
          }
        }
      }
    )
    if (success) {
      if (path.isEmpty())
        Redirect(routes.Application.home).withSession(Security.username -> userName, "userId" -> userId, "name" -> firstName)
      else
        Redirect(path).withSession(Security.username -> userName, "userId" -> userId, "name" -> firstName)
    } 
    else 
      Redirect(routes.AuthController.login()).flashing("error" -> "Invalid email or password")
  }
  
  def logout = Action {
    Redirect(routes.AuthController.login).withNewSession.flashing(
      "success" -> "You are now logged out."
    )
  }
}