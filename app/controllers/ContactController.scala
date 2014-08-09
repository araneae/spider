package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import play.api.libs.json._
import play.api.mvc.Controller
import traits.Secured
import models.repositories._
import models.tables._
import enums._
import services._
import utils._
import models.dtos._

object ContactController extends Controller with Secured {
  
  private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  private final val BLANK = ""
  
  def getAll = IsAuthenticated{ username => implicit request =>
      logger.info("in ContactController.get...")
      println("in ContactController.get...")
      var list = ContactRepository.findAll(userId)
      val data = Json.toJson(list)
      Ok(data).as(JSON)
  }
  
  def get(contactUserId: Int) = IsAuthenticated{ username => implicit request =>
      logger.info("in ContactController.get(${contactUserId})...")
      println("in ContactController.get(${contactUserId})...")
      var contact = ContactRepository.find(userId, contactUserId)
      val data = Json.toJson(contact)
      Ok(data).as(JSON)
  }
  
  def create(contactUserId: Int) = IsAuthenticated(parse.json){ username => implicit request =>
      logger.info("in ContactController.create...")
      println("in ContactController.create...")
      UserRepository.find(contactUserId).map{ contactUser =>
        // merge userId with the request object
        val token = TokenGenerator.token
        
        // check if the contact already exist
        ContactRepository.find(userId, contactUserId).map{ myContact =>
            if (myContact.status == ContactStatus.PENDING)
            { // update token
                ContactRepository.updateToken(userId, contactUserId, token)
                EmailService.inviteContact(contactUser, name, token, Configuration.applicationBaseUrl)
                Ok(HttpResponseUtil.success("Successfully invited!"))
            }
            else
              Ok(HttpResponseUtil.success("Ignored, already in connection!"))
        }
        .getOrElse{
            val myNewContact = Contact(userId, contactUserId, ContactStatus.PENDING, Some(token))
            ContactRepository.create(myNewContact)
            // send invitation email (should be used Actor)
            EmailService.inviteContact(contactUser, name, token, Configuration.applicationBaseUrl)
            Ok(HttpResponseUtil.success("Successfully invited!"))
        }
      }
      .getOrElse(NotFound)
  }
  
  def delete(contactUserId: Int) = IsAuthenticated{ username => implicit request =>
      logger.info("in ContactController.delete...")
      println("in ContactController.delete...")
      ContactRepository.delete(userId, contactUserId);
      Ok(HttpResponseUtil.success("Successfully deleted!"))
  }
  
  def search(email: String) = IsAuthenticated{ username => implicit request =>
      logger.info("in ContactController.search...")
      println("in ContactController.search...")
      val user = UserRepository.findByEmail(email);
      user match {
        case Some(u) =>
                  val safeUser = User(Some(u.userId.get), u.firstName, u.lastName, u.email, BLANK)
                  val list = List(safeUser)
                  val text = Json.toJson(list)
                  Ok(text).as(JSON)
        case None => // empty result
                 Ok(BLANK)
      }
  }
  
  def accept(token: String) = IsAuthenticated{ username => implicit request =>
      logger.info("in ContactController.accept...")
      println("in ContactController.accept...")
      // find the contact entry in the database
      ContactRepository.findByToken(token).map{ contact =>
        // verify the logged in user
        UserRepository.find(contact.contactUserId).map{ contactUser =>
            contactUser.userId.map{ myUserId =>
              if (myUserId == userId){
                // update contact entry
                ContactRepository.updateStatus(contact.userId, myUserId, ContactStatus.CONNECTED, None)
                AdviserRepository.find(userId, contact.userId).map{ adviser =>
                  // already connected
                  Redirect(routes.Application.home)
                }.getOrElse{
                  // create adviser entry
                  val myAdviser = Adviser(userId, contact.userId, ContactStatus.CONNECTED, None)
                  AdviserRepository.create(myAdviser)
                  Redirect(routes.Application.home)
                }
              }
              else
              { // the user is trying to use someone else's token
                Redirect(routes.AuthController.login)
              }
            }
            .getOrElse(NotFound) // contact userId is not set in the object
        }
        .getOrElse(NotFound) // contact user is not found in the database
      }
      .getOrElse(Redirect(routes.Application.home)) // user is already connected
  }
}