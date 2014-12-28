package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import play.api.libs.json._
import play.api.mvc.Controller
import scala.concurrent.Future
import scala.collection.mutable.ListBuffer
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Await
import java.util.concurrent.TimeUnit
import akka.pattern.ask
import akka.util.Timeout
import traits.Secured
import models.repositories._
import models.tables._
import enums._
import services._
import utils._
import models.dtos._
import traits.AkkaActor
import actors._

object ContactController extends Controller with Secured with AkkaActor {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  private final val BLANK = ""
  
  def getAll = IsAuthenticated{ username => implicit request =>
      //logger.info("in ContactController.getAll...")
      println("in ContactController.getAll")
      var list = ContactRepository.getAll(userId)
      val data = Json.toJson(list)
      Ok(data).as(JSON)
  }
  
  def get(contactUserId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in ContactController.get(${contactUserId})...")
      println(s"in ContactController.get(${contactUserId})")
      var contact = ContactRepository.findContact(userId, contactUserId)
      val data = Json.toJson(contact)
      Ok(data).as(JSON)
  }
  
  def delete(contactUserId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in ContactController.delete...")
      println(s"in ContactController.delete(${contactUserId})")
      ContactRepository.delete(userId, contactUserId);
      Ok(HttpResponseUtil.success("Successfully deleted!"))
  }
  
  def search(searchText: String) = IsAuthenticated{ username => implicit request =>
      //logger.info(s"in ContactController.search(${searchText})")
      println(s"in ContactController.search(${searchText})")

      implicit val timeout = Timeout(MESSAGE_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
      // send message to index searcher
      val f = ask(indexSearcherActor, MessageUserSearch(searchText)).mapTo[MessageUserSearchResult]
      val result = f.map {
           case MessageUserSearchResult(userIds) => {
                 var list = new ListBuffer[ContactDTO]()
                  userIds.map { uId =>
                    if (uId != userId) {
                      // check if the user is already connected
                      val optContact = ContactRepository.findContact(userId, uId)
                      optContact match {
                        case Some(contact) =>
                    	    list += contact
                        case None =>
                          val optUserProfilePersonal = UserRepository.findUserProfilePersonal(uId)
                          optUserProfilePersonal match {
                            case Some((u, up)) =>
                              val pictureUrl = up.physicalFile match {
                                                    case Some(physicalFile) => Configuration.userProfilePictureUrl(physicalFile)
                                                    case None => Configuration.defaultProfilePictureUrl
                                                }
                              val contact = ContactDTO(u.userId.get, pictureUrl, u.firstName, u.lastName, ContactStatus.NOTCONNECTED)
                              list += contact
                            case _ =>    
                          }
                      }
                    }
                  }
                  val text = Json.toJson(list)
                  Ok(text).as(JSON)
            }
           case _ => Ok("").as(JSON)
      }
  
    Await.result(result, timeout.duration)
  }
  
  def accept(token: String) = IsAuthenticated{ username => implicit request =>
      //logger.info("in ContactController.accept...")
      println(s"in ContactController.accept(${token})")
      // find the contact entry in the database
      ContactRepository.findByToken(token).map { contact =>
        // verify the logged in user
        if (contact.contactUserId == userId) {
          // update contact entry
          ContactRepository.updateStatus(contact.userId, userId, ContactStatus.CONNECTED, None)
          val optMyContact = ContactRepository.find(userId, contact.userId)
          optMyContact match {
            case Some(myContact) =>
                 if (myContact.status != ContactStatus.CONNECTED) {
                   ContactRepository.updateStatus(userId, contact.userId, ContactStatus.CONNECTED, None)
                 }
            case None =>
                val myNewContact = Contact(userId, contact.userId, ContactStatus.CONNECTED, None, userId)
                ContactRepository.create(myNewContact)
            }
          
            Redirect(routes.Application.home)
        }
        else { // the user is trying to use someone else's token
          Redirect(routes.Application.home)
        }
      }
      .getOrElse(Redirect(routes.Application.home)) // user is already connected
  }
  
  def inviteMessage(contactUserId: Long) = IsAuthenticated{ username => implicit request =>
    //logger.info("in ContactController.inviteMessage()")
    println(s"in ContactController.inviteMessage(${contactUserId})")
    val optUser = UserRepository.find(contactUserId)
    optUser match {
       case Some(user) =>
          val subject = s"Hello ${user.firstName}"
          val message = s"Hi ${user.firstName}, \nI would like to get connected with you. Please accept my invitation.\nThanks,\n${name}"
          val contactInvite = ContactInvite(contactUserId, subject, message)
          val data = Json.toJson(contactInvite)
          Ok(data).as(JSON)
      case None =>
          Ok("").as(JSON)
    }
  }
  
  def invite(contactUserId: Long) = IsAuthenticated(parse.json){ username => implicit request =>
    //logger.info("in ContactController.invite()")
    println(s"in ContactController.invite(${contactUserId})")
    val jsonObj = request.body.asInstanceOf[JsObject]
    jsonObj.validate[ContactInvite].fold(
        valid = { contactInvite =>
           val optContactuserId = UserRepository.find(contactUserId)
           optContactuserId match {
               case Some(contactUser) =>
                  val token = TokenGenerator.token
                   // check if the contact already exist
                  val optExistingContact = ContactRepository.find(userId, contactUserId)
                  optExistingContact match { 
                    case Some(existingContact) =>
                        if (existingContact.status != ContactStatus.CONNECTED) {
                           // update token
                           ContactRepository.updateToken(userId, contactUserId, token)
                           val optOtherContact = ContactRepository.find(contactUserId, userId)
                           optOtherContact match {
                              case Some(otherContact) =>
                                  ContactRepository.updateStatus(contactUserId, userId, ContactStatus.AWAITING, None)
                              case None =>  
                                  val newContact = Contact(contactUserId, userId, ContactStatus.AWAITING, None, userId)
                                  ContactRepository.create(newContact)
                           }
                           MessageService.send(userId, None, contactInvite.subject, contactInvite.message, List(contactUserId))
                           EmailService.inviteContact(contactUser, name, token)
                           Ok(HttpResponseUtil.success("Successfully invited!"))
                        }
                        else
                          Ok(HttpResponseUtil.success("Ignored, already connected!"))
                    case None =>
                        val newContact = Contact(userId, contactUserId, ContactStatus.PENDING, Some(token), userId)
                        ContactRepository.create(newContact)
                        val optOtherContact = ContactRepository.find(contactUserId, userId)
                        optOtherContact match {
                          case Some(otherContact) =>
                              ContactRepository.updateStatus(contactUserId, userId, ContactStatus.AWAITING, None)
                          case None =>  
                              val newContact = Contact(contactUserId, userId, ContactStatus.AWAITING, None, userId)
                              ContactRepository.create(newContact)
                        }
                        // send invitation email (should be used Actor)
                        MessageService.send(userId, None, contactInvite.subject, contactInvite.message, List(contactUserId))
                        EmailService.inviteContact(contactUser, name, token)
                        Ok(HttpResponseUtil.success("Successfully invited!"))
                  }
               case None =>
                 Ok(HttpResponseUtil.error("User not found!"))
          }
        }
        ,invalid = { errors => 
             Ok(HttpResponseUtil.error("Unable to parse payload"))
          }
        )
  }
  
  def acceptInvitation(contactUserId: Long) = IsAuthenticated{ username => implicit request =>
    //logger.info("in ContactController.acceptInvitation()")
    println(s"in ContactController.acceptInvitation(${contactUserId})")
    val optContact = ContactRepository.find(userId, contactUserId)
    optContact match { 
      case Some(contact) =>
          ContactRepository.updateStatus(userId, contactUserId, ContactStatus.CONNECTED, None)
          ContactRepository.updateStatus(contactUserId, userId, ContactStatus.CONNECTED, None)
          Ok(HttpResponseUtil.success("Successfully accepted invitation!"))
      case None =>
          Ok(HttpResponseUtil.error("Contact not found!"))
    }
  }
  
  def rejectInvitation(contactUserId: Long) = IsAuthenticated{ username => implicit request =>
    //logger.info("in ContactController.rejectInvitation()")
    println(s"in ContactController.rejectInvitation(${contactUserId})")
    val optContact = ContactRepository.find(userId, contactUserId)
    optContact match { 
      case Some(contact) =>
          ContactRepository.delete(userId, contactUserId)
          ContactRepository.updateStatus(contactUserId, userId, ContactStatus.REJECTED, None)
          Ok(HttpResponseUtil.success("Successfully rejected invitation!"))
      case None =>
          Ok(HttpResponseUtil.error("Contact not found!"))
    }
  }
}