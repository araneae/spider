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
      var list = ContactRepository.findAll(userId)
      val data = Json.toJson(list)
      println("data "+ data)
      Ok(data).as(JSON)
  }
  
  def get(contactUserId: Int) = IsAuthenticated{ username => implicit request =>
      //logger.info("in ContactController.get(${contactUserId})...")
      println(s"in ContactController.get(${contactUserId})")
      var contact = ContactRepository.find(userId, contactUserId)
      val data = Json.toJson(contact)
      Ok(data).as(JSON)
  }
  
  def create(contactUserId: Int) = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in ContactController.create...")
      println(s"in ContactController.create(${contactUserId}).")
      UserRepository.find(contactUserId).map{ contactUser =>
        // merge userId with the request object
        val token = TokenGenerator.token
        
        // check if the contact already exist
        ContactRepository.find(userId, contactUserId).map{ myContact =>
            if (myContact.status != ContactStatus.CONNECTED){
                // update token
                ContactRepository.updateToken(userId, contactUserId, token)
                EmailService.inviteContact(contactUser, name, token)
                Ok(HttpResponseUtil.success("Successfully invited!"))
            }
            else
              Ok(HttpResponseUtil.success("Ignored, already in connection!"))
        }
        .getOrElse{
            val myNewContact = Contact(userId, contactUserId, ContactStatus.PENDING, Some(token), userId)
            ContactRepository.create(myNewContact)
            // send invitation email (should be used Actor)
            EmailService.inviteContact(contactUser, name, token)
            Ok(HttpResponseUtil.success("Successfully invited!"))
        }
      }
      .getOrElse(NotFound)
  }
  
  def delete(contactUserId: Int) = IsAuthenticated{ username => implicit request =>
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
                 var list = new ListBuffer[ContactFull]()
                  userIds.map { uId =>
                    if (uId != userId) {
                      // check if the user is already connected
                      val contacts = ContactRepository.findContact(userId, uId)
                      if (contacts.length > 0) {
                    	  list += contacts(0)
                      }
                      else {
                        val user = UserRepository.find(uId)
                        user match {
                          case Some(u) =>
                              val contact = ContactFull(u.userId.get, u.firstName, u.lastName, u.email, ContactStatus.NOTCONNECTED)
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
}