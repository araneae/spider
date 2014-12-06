package controllers

import org.mindrot.jbcrypt.BCrypt
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import play.api.data.Form
import play.api.data.Forms.nonEmptyText
import play.api.data.Forms.tuple
import org.joda.time.DateTime
import play.api.db.slick._
import play.api.mvc.Action
import play.api.mvc.Controller
import play.api.mvc.Security
import play.api.libs.json._
import models.repositories._
import traits._
import utils._
import models.dtos._
import services._

object UserProfileController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def get = IsAuthenticated{ username => implicit request =>
      //logger.info("in UserProfileController.get")
      println("in UserProfileController.get")
      var optUser = UserRepository.find(userId)
      optUser match {
        case Some(user) =>
            user.userProfilePersonalId match {
              case Some(userProfilePersonalId) =>
                val optUserProfile = UserProfilePersonalRepository.find(userProfilePersonalId)
                optUserProfile match {
                  case Some(userProfile) =>
                    val userProfileDTO = UserProfileDTO(userProfile.userProfilePersonalId, 
                                            user.email, 
                                            userProfile.xrayTerms,
                                            userProfile.aboutMe,
                                            userProfile.picture,
                                            userProfile.mobile,
                                            userProfile.alternateEmail,
                                            userProfile.gender,
                                            userProfile.maritalStatus,
                                            userProfile.birthYear,
                                            userProfile.birthMonth,
                                            userProfile.birthDay)
                    val text = Json.toJson(userProfileDTO)
                    Ok(text).as(JSON)
                  case None =>
                    // user profile object is not found -- error
                    BadRequest(HttpResponseUtil.error("Unable to find user profile!"))
                }
              case None =>
                // user profile object hasn't been created yet - expected
               Ok(HttpResponseUtil.reponseEmptyObject())
            } 
        case None => 
          // currently logged-in user is not found -- error
          BadRequest(HttpResponseUtil.error("Unable to find user!"))
      }
  }
  
  def save = IsAuthenticated(parse.json){ username => implicit request =>
    //logger.info("in UserProfileController.save")
    println("in UserProfileController.save")
    
    val jsonObj = request.body.asInstanceOf[JsObject]
    jsonObj.validate[UserProfileDTO].fold(
        valid = { userProfileDTO =>
            val optUserProfile = UserProfilePersonalRepository.find(userProfileDTO.userProfilePersonalId.get)
            optUserProfile match {
              case Some(userProfile) =>
                  val updatedUserProfile = UserProfilePersonal(
                                  userProfileDTO.userProfilePersonalId,
                                  userProfileDTO.xrayTerms,
                                  userProfileDTO.aboutMe,
                                  userProfileDTO.picture,
                                  userProfileDTO.mobile,
                                  userProfileDTO.alternateEmail,
                                  userProfileDTO.gender,
                                  userProfileDTO.maritalStatus,
                                  userProfileDTO.birthYear,
                                  userProfileDTO.birthMonth,
                                  userProfileDTO.birthDay,
                                  userProfile.createdAt,
                                  Some(new DateTime())
                                )
                UserProfilePersonalRepository.update(updatedUserProfile)
                Ok(HttpResponseUtil.success("Successfully updated user settings.")).as(JSON)
              case None =>
                BadRequest(HttpResponseUtil.error("Unable to find user settings"))
            }
        },
        invalid = {
          errors => BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
        }
     )
  }

  def sendEmailVerificationCode = IsAuthenticated(parse.json){ username => implicit request =>
    //logger.info("in UserProfileController.sendEmailVerificationCode")
    println("in UserProfileController.sendEmailVerificationCode")
      
    val jsonObj = request.body.asInstanceOf[JsObject]
    val optEmail = (jsonObj \ "email").asOpt[String]
    
    optEmail match {
      case Some(email) =>
            val optUser = UserRepository.find(userId)
            optUser match {
              case Some(user) =>
                val otp = TokenGenerator.otp
                EmailService.sendChangeEmailOtp(user.firstName, email, otp)
                val expiryTime = new DateTime().plusMinutes(Configuration.otpPasswordTimeoutInMins)
                val encryptedOtp = BCrypt.hashpw(otp, BCrypt.gensalt());
                UserRepository.updateOneTimePassword(user.userId.get, Some(encryptedOtp), Some(expiryTime))
                Ok(HttpResponseUtil.success(s"Successfully sent verification email, please update your email within next ${Configuration.otpPasswordTimeoutInMins} minutes."))
              case None => 
                // currently logged-in user is not found -- error
                BadRequest(HttpResponseUtil.error("Unable to find user!"))
            }
      case None =>
        BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
    }
  }
  
  def updateEmail = IsAuthenticated(parse.json){ username => implicit request =>
    //logger.info("in UserProfileController.updateEmail")
    println("in UserProfileController.updateEmail")
      
    val jsonObj = request.body.asInstanceOf[JsObject]
    val optCode = (jsonObj \ "code").asOpt[String]
    val optEmail = (jsonObj \ "email").asOpt[String]
    
    optCode match {
      case Some(code) =>
         optEmail match {
           case Some(email) =>
              val optUser = UserRepository.find(userId)
              optUser match {
                case Some(user) =>
                  user.otp match {
                    case Some(otp) =>
                        val now = DateTime.now()
                        if (BCrypt.checkpw(code, otp) &&
                            now.compareTo(user.otpExpiredAt.get) < 0) {
                            UserRepository.updateEmail(userId, email)
                            Ok(HttpResponseUtil.success("Successfully updated your email."))
                        }
                        else {
                           // either code doesn't match or otp already expired 
                           BadRequest(HttpResponseUtil.error("Unable to update email!"))
                        }
                    case None =>
                        // unable to find otp
                        BadRequest(HttpResponseUtil.error("Unable to find otp!"))
                    }
                case None => 
                  // currently logged-in user is not found -- error
                  BadRequest(HttpResponseUtil.error("Unable to find user!"))  
              }
           case None =>
              // unable to parse payload
              BadRequest(HttpResponseUtil.error("Unable to parse payload!"))  
         }
      case None =>
        // unable to parse payload
        BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
    }
  }
}