package controllers

import org.mindrot.jbcrypt.BCrypt
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick._
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.mvc.Security
import models.repositories.UserRepository
import play.api.mvc.Flash
import traits._
import utils._
import services.EmailService
import org.joda.time.DateTime
import models.repositories.CompanyRepository

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
  
  val passwordRecoveryForm = Form(
      "email" -> nonEmptyText
  )

  val passwordResetForm = Form(
    tuple(
      "email" -> nonEmptyText,
      "otp" -> nonEmptyText,
      "password" -> text(minLength=6, maxLength=16),
      "confirmPassword" -> nonEmptyText
    ) verifying ("Passwords don't match", result => result match {
       case (email, otp, password, confirmPassword) => checkPasswords(password, confirmPassword)
    })
  )
  
  def check(username: String, password: String) = {
    (username.length() > 0 && password.length() > 0)  
  }
  
  def checkPasswords(password: String, confirmPassword: String) = {
    (password == confirmPassword)  
  }

  def login = Action { 
    implicit request =>
      Ok(views.html.login(Configuration.applicationTitle){loginForm})
  }

  def authenticate = Action { 
    implicit request =>
      val formData = loginForm.bindFromRequest
      formData.fold(
        formWithErrors => {
          Ok(views.html.login(Configuration.applicationTitle)(formData))
        },
        value => {
          value match {
            case Tuple2(email, password) => {
              //logger.info(s"$email $password")
              UserRepository.findByEmail(email).map { 
                user => {
                  val userName = user.email
                  val userId = user.userId.get.toString
                  val firstName = user.firstName
                  val token = user.activationToken
                  
                  if (BCrypt.checkpw(password, user.password)) {
                      if (!user.verified) {
                        Redirect(routes.AuthController.login).flashing(Flash(formData.data) + 
                                      ("error" -> "Account verification pending.") +
                                      ("html" -> s"Click <a href='/activation/${token}/resend'>here</a> to resend activation email."))
                      } 
                      else {
                        UserRepository.updateLastLogon(user.userId.get)
                        //if (path.isEmpty())
                          Redirect(routes.Application.home).withSession(Security.username -> userName, 
                                                                        "userId" -> userId,
                                                                        "name" -> firstName)
                        //else
                        //  Redirect(path).withSession(Security.username -> userName, 
                        //                             "userId" -> userId,
                        //                             "name" -> firstName)
                      }
                    }
                    else {
                      // user does exist in the system, show up password recovery link
                      Redirect(routes.AuthController.login()).flashing(Flash(formData.data) +
                                      ("error" -> "Invalid email or password") +
                                      ("html" -> s"Forgot your password ? Click <a href='/login/password/${token}/recover'>here</a> to recover your password."))
                    }
                }
              }.getOrElse(
                Redirect(routes.AuthController.login()).flashing(Flash(formData.data) + ("error" -> "Invalid email or password"))
              )
            }
          case _ =>  
                Redirect(routes.AuthController.login()).flashing(Flash(formData.data) + ("error" -> "Invalid email or password"))
        }
      }
    )
  }
  
  def resend(token: String) = Action {
    implicit request =>
      val optUser = UserRepository.findByActivationToken(token)
      optUser match {
        case Some(user) =>
          EmailService.sendWelcomeEmail(user)
          Redirect(routes.AuthController.login).flashing("success" -> "Successfully resent activation email.")
        case None =>
          Redirect(routes.Application.signup).flashing("error" -> "Invalid request, please sign-up.")
      }
  }
  
  def activate(token: String) = Action {
    implicit request =>
      val optUser = UserRepository.findByActivationToken(token)
      optUser match {
        case Some(user) =>
          UserRepository.activate(user.userId.get)
          Redirect(routes.AuthController.login).flashing("success" -> "Successfully activated account.")
        case None =>
          Redirect(routes.Application.signup).flashing(("error" -> "Invalid request, please signup."))
      }
  }
  
  def formRecoverPassword(token: String) = Action { 
    implicit request =>
      val optUser = UserRepository.findByActivationToken(token)
      optUser match {
         case Some(user) =>
            Ok(views.html.recoverPassword(Configuration.applicationTitle)(Configuration.applicationName)
                                                                    (token)(passwordRecoveryForm.bind(Map("email" ->  user.email))))
         case None =>
           Ok(views.html.recoverPassword(Configuration.applicationTitle)(Configuration.applicationName)(token)(passwordRecoveryForm))
      }
  }
  
  def recoverPassword(token: String) = Action { 
    implicit request =>
      
    val formData = passwordRecoveryForm.bindFromRequest
    formData.fold(
      formWithErrors => {
        Ok(views.html.recoverPassword(Configuration.applicationTitle)(Configuration.applicationName)(token)(formData))
      },
      email => {
          val optUser = UserRepository.findByActivationToken(token)
          optUser match {
            case Some(user) =>
              if (user.email == email) {
                val otp = TokenGenerator.otp
                EmailService.sendOtpEmail(user, otp)
                
                val expiryTime = new DateTime().plusMinutes(Configuration.otpPasswordTimeoutInMins)
                val encryptedOtp = BCrypt.hashpw(otp, BCrypt.gensalt());
                val token = user.activationToken
                
                UserRepository.updateOneTimePassword(user.userId.get, Some(encryptedOtp), Some(expiryTime))
                Redirect(routes.AuthController.resetPassword(token)).flashing(
                                                               ("success" -> "Your one time password has been sent by email. Please enter your password within 10 minutes."))
              }
              else {
                Redirect(routes.AuthController.formRecoverPassword(token)).flashing(Flash(formData.data) + 
                                                              ("error" -> "Invalid email address.") +
                                                              ("html" -> "If you don't have an account, click <a href='/signup'>here</a> to signup."))
              }
            case None =>
              Redirect(routes.AuthController.formRecoverPassword(token)).flashing(Flash(formData.data) + 
                                                              ("error" -> "Invalid email address.") +
                                                              ("html" -> "Click <a href='/signup'>here</a> to signup."))
          }
      }
    )
  }
  
  def formResetPassword(token: String) = Action {
    implicit request =>
      val optUser = UserRepository.findByActivationToken(token)
      optUser match {
        case Some(user) =>
          Ok(views.html.resetPassword(Configuration.applicationTitle)(Configuration.applicationName)
                  (token)(passwordResetForm.bind(Map("email" -> user.email))))
        case None =>
          Ok(views.html.resetPassword(Configuration.applicationTitle)(Configuration.applicationName)(token)(passwordResetForm))
      }
  }
  
  def resetPassword(token: String) = Action {
    implicit request =>
    val formData = passwordResetForm.bindFromRequest
    formData.fold(
      formWithErrors => {
        Ok(views.html.resetPassword(Configuration.applicationTitle)(Configuration.applicationName)(token)(formData))
      },
      value => {
        value match {
          case Tuple4(email, otp, password, confirmPassword) => {
              val optUser = UserRepository.findByEmail(email)
              optUser match {
                case Some(user) => {
                   user.otp match {
                     case Some(userOtp) =>
                       val now = DateTime.now()
                       if (BCrypt.checkpw(otp, userOtp)) {
                         if (now.compareTo(user.otpExpiredAt.get) < 0) {
                            val encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                            UserRepository.resetPassward(user.userId.get, encryptedPassword)
                            Redirect(routes.AuthController.login()).flashing(("success" -> "Successfully reset password."))
                         }
                         else {
                           Redirect(routes.AuthController.formResetPassword(token)).flashing(Flash(formData.data) + 
                                      ("error" -> "One time password expired.") +
                                      ("html" -> s"Click <a href='/login/password/${token}/recover'>here</a> to re-generate one time password."))
                         }
                       }
                     else {
                      Redirect(routes.AuthController.formResetPassword(token)).flashing(Flash(formData.data) + 
                                    ("error" -> "One time password doesn't match."))
                     }
                     case None =>
                       Redirect(routes.AuthController.formResetPassword(token)).flashing(Flash(formData.data) + 
                                      ("error" -> "One time password expired.") +
                                      ("html" -> s"Click <a href='/login/password/${token}/recover'>here</a> to re-generate one time password."))
                     
                   }
                }
                case None =>
                  Redirect(routes.AuthController.formResetPassword(token)).flashing(Flash(formData.data) + 
                                                                ("error" -> "Account not found.") +
                                                                ("html" -> "If you don't have an account, click <a href='/signup'>here</a> to signup."))
              }
          }
          case _ =>
            Redirect(routes.AuthController.formResetPassword(token)).flashing(Flash(formData.data) + 
                                                                ("error" -> "Invalid request.") +
                                                                ("html" -> "If you don't have an account, click <a href='/signup'>here</a> to signup."))
        }
      }
    )
  }
  
  def logout = Action {
    Redirect(routes.AuthController.login).withNewSession.flashing(
      "success" -> "You are now logged out."
    )
  }
}