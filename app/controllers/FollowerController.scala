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

object FollowerController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  private final val BLANK = ""
    
  def getAll = IsAuthenticated{ username => implicit request =>
      //logger.info("in FollowerController.getAll...")
      println("in FollowerController.getAll...")
      var list = FollowerRepository.findAll(userId)
      val data = Json.toJson(list)
      Ok(data).as(JSON)
  }
  
  def get(followerId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in FollowerController.get(${followerId})...")
      println("in FollowerController.get(${followerId})...")
      var follower = FollowerRepository.find(userId, followerId)
      val data = Json.toJson(follower)
      Ok(data).as(JSON)
  }
  
  def create(followerId: Long) = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in FollowerController.creater...")
      println("in FollowerController.create...")
      UserRepository.find(followerId).map{ followerUser =>
        // merge userId with the request object
        val token = TokenGenerator.token
        
        // check if the follower already exist
        FollowerRepository.find(userId, followerId).map{ myFollower =>
            if (myFollower.status == ContactStatus.PENDING)
            { // update token
                FollowerRepository.updateToken(userId, followerId, token)
                EmailService.inviteSubject(followerUser, name, token)
                Ok("Updated")
            }
            else
              Ok("Ignored")
        }
        .getOrElse{
            val myNewFollower = Follower(userId, followerId, ContactStatus.PENDING, Some(token), userId)
            FollowerRepository.create(myNewFollower)
            // send invitation email (should be used Actor)
            EmailService.inviteSubject(followerUser, name, token)
            Ok("Created")
        }
      }
      .getOrElse(NotFound)
  }
  
  def delete(followerId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in FollowerController.delete...")
      println("in FollowerController.delete...")
      FollowerRepository.delete(userId, followerId);
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
      //logger.info("in FollowerController.accept...")
      println("in FollowerController.accept...")
      // find the follower entry in the database
      FollowerRepository.findByToken(token).map{ follower =>
        // verify the logged in user
        UserRepository.find(follower.followerId).map{ followerUser =>
            followerUser.userId.map{ myUserId =>
              if (myUserId == userId){
                // update follower entry
                FollowerRepository.updateStatus(follower.subjectId, userId, ContactStatus.CONNECTED, None)
                FollowerRepository.find(userId, follower.followerId).map{ follower =>
                  // already exists
                  Ok("Already connected")
                }.getOrElse{
                  // create follower entry
                   val myFollower = Follower(userId, follower.followerId, ContactStatus.CONNECTED, None, userId)
                   FollowerRepository.create(myFollower)
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