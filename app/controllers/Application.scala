package controllers

import org.mindrot.jbcrypt.BCrypt
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import models.dtos.User
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick._
import play.api.mvc.Action
import play.api.mvc.Controller
import traits._
import play.api.mvc.Flash
import models.repositories._
import models.dtos._
import actors._
import utils._
import enums._
import services._
import enums.OwnershipType._
import enums.OwnershipType
import org.joda.time.DateTime

object Application extends Controller with Secured with AkkaActor {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  val tup = tuple(
      "first_name" -> nonEmptyText,
      "last_name" -> optional(text),
      "last_name" -> nonEmptyText,
      "email" -> nonEmptyText,
      "password" -> text(minLength=6, maxLength=16),
      "confirmPassword" -> text,
      "countryCode" -> nonEmptyText
    ).verifying (
        "Passwords don't match", data => data._5 == data._6
    )
  
  val signupForm = Form (tup)
  
  def index = Action { implicit request =>
    //logger.info("Application.index...")
    val countries = CountryRepository.findAllAvailable
    Ok(views.html.index(Configuration.applicationTitle)(Configuration.applicationName)(countries)(signupForm))
  }
  
  def home = IsAuthenticated { username => implicit request =>
    Ok(views.html.home(Configuration.applicationTitle)(s"Welcome back ${name}"))
  }
  
  def signup = Action { implicit request =>
    //logger.info("Application.signup...")
    val form = if (request.flash.get("error").isDefined) 
                  signupForm.bind(request.flash.data)
                else
                  signupForm
    val countries = CountryRepository.findAllAvailable
    Ok(views.html.signup(Configuration.applicationTitle)(Configuration.applicationName)(countries)(form))
  }
  
  def register = Action {
    implicit request => 
       //logger.info("Application.register...") 
       val formData = signupForm.bindFromRequest
       formData.fold(
          hasErrors = { form =>
            val countries = CountryRepository.findAllAvailable
            BadRequest(views.html.signup(Configuration.applicationTitle){Configuration.applicationName}{countries}{form})
          },
          success = { value => 
            //logger.info(s"register $value") 
            value match {
              case Tuple7(first_name, middle_name, last_name, email, password, confirmPassword, countryCode) => {
                // find if the user already exists
                val existingUser = UserRepository findByEmail email
                existingUser match {
                  case Some (u) => {
                     Redirect(routes.Application.signup).flashing(Flash(formData.data) + ("error" -> "Email already exists"))
                  }
                  case None => {
                      val optCountry = CountryRepository.findByCode(countryCode)
                      optCountry match {
                        case Some(country) =>
                          val token = TokenGenerator.token
                          val encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
                          val userProfilePersonalId = UserProfilePersonalRepository.create(UserProfilePersonal(None, Configuration.defaultXrayTerms))
                          val user = User(None, first_name, middle_name, last_name, email, None, encryptedPassword, country.countryId.get, token, false, new DateTime(), UserStatusType.PENDING, Some(userProfilePersonalId))
                          val userId = UserRepository create user
                        
                          val savedUser = UserRepository find userId
                          savedUser match {
                            case Some(user) => 
                              indexWriterActor ! MessageAddUser(user)
                              EmailService.sendWelcomeEmail(user)
                            case None =>
                          }
                          // create default message boxes
                          MessageBoxRepository.createDefaults(userId, userId)
                          val documentFolderId = DocumentFolderRepository.create(DocumentFolder(None, "Documents", true, userId))
                          UserDocumentFolderRepository.create(UserDocumentFolder(None, documentFolderId, userId, OwnershipType.OWNED, true, true, true, false, None, userId))
                          Redirect(routes.AuthController.login).flashing(Flash(formData.data) + ("success" -> "Activation email has been sent."))
                        case None =>
                          Redirect(routes.Application.signup).flashing(Flash(formData.data) + ("error" -> "Country is not available"))
                      }
                  }
                }
              }
            }
          }
        )
  }
}