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
import play.api.Play
import models.dtos._

object AdviserController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  private final val BLANK = ""
    
  def getAll = IsAuthenticated{ username => implicit request =>
      //logger.info("in AdviserController.getAll...")
      println("in AdviserController.getAll...")
      var list = AdviserRepository.findAll(userId)
      val data = Json.toJson(list)
      Ok(data).as(JSON)
  }
  
  def get(adviserUserId: Int) = IsAuthenticated{ username => implicit request =>
      //logger.info("in AdviserController.get(${adviserUserId})...")
      println("in AdviserController.get(${adviserUserId})...")
      var adviser = AdviserRepository.find(userId, adviserUserId)
      val data = Json.toJson(adviser)
      Ok(data).as(JSON)
  }
  
  def create(adviserUserId: Int) = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in AdviserController.creater...")
      println("in AdviserController.create...")
      UserRepository.find(adviserUserId).map{ adviserUser =>
        // merge userId with the request object
        val token = TokenGenerator.token
        
        // check if the adviser already exist
        AdviserRepository.find(userId, adviserUserId).map{ myAdviser =>
            if (myAdviser.status == ContactStatus.PENDING)
            { // update token
                AdviserRepository.updateToken(userId, adviserUserId, token)
                EmailService.inviteAdviser(adviserUser, name, token)
                Ok("Updated")
            }
            else
              Ok("Ignored")
        }
        .getOrElse{
            val myNewAdviser = Adviser(userId, adviserUserId, ContactStatus.PENDING, Some(token), userId)
            AdviserRepository.create(myNewAdviser)
            // send invitation email (should be used Actor)
            EmailService.inviteAdviser(adviserUser, name, token)
            Ok("Created")
        }
      }
      .getOrElse(NotFound)
  }
  
  def delete(adviserUserId: Int) = IsAuthenticated{ username => implicit request =>
      //logger.info("in AdviserController.delete...")
      println("in AdviserController.delete...")
      AdviserRepository.delete(userId, adviserUserId);
      Ok("Deleted")
  }
  
  def search(email: String) = IsAuthenticated{ username => implicit request =>
      //logger.info("in search...")
      println("in search...")
      val user = UserRepository.findByEmail(email);
      user match {
        case Some(u) =>
//                  val safeUser = User(Some(u.userId.get), u.firstName, u.lastName, u.email, BLANK)
//                  val list = List(safeUser)
//                  val text = Json.toJson(list)
//                  Ok(text).as(JSON)
                 Ok("").as(JSON)
        case None => // empty result
                 Ok(BLANK)
      }
  }
  
  def accept(token: String) = IsAuthenticated{ username => implicit request =>
      //logger.info("in AdviserController.accept...")
      println("in AdviserController.accept...")
      // find the adviser entry in the database
      AdviserRepository.findByToken(token).map{ adviser =>
        // verify the logged in user
        UserRepository.find(adviser.adviserUserId).map{ adviserUser =>
            adviserUser.userId.map{ myUserId =>
              if (myUserId == userId){
                // update adviser entry
                AdviserRepository.updateStatus(adviser.userId, userId, ContactStatus.CONNECTED, None)
                AdviserRepository.find(userId, adviser.userId).map{ adviser =>
                  // already exists
                  Ok("Already connected")
                }.getOrElse{
                  // create adviser entry
                   val myAdviser = Adviser(userId, adviser.userId, ContactStatus.CONNECTED, None, userId)
                   AdviserRepository.create(myAdviser)
                   Ok("Ok")
                }
              }
              else
              {
                Redirect(routes.AuthController.login)
              }
            }
            .getOrElse(NotFound)
        }
        .getOrElse(NotFound)
      }
      .getOrElse(NotFound)
  }
}