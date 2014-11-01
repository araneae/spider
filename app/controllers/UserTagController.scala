package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import models.dtos.UserTag
import models.repositories.UserTagRepository
import models.repositories.DocumentTagRepository
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Controller
import traits.Secured
import utils.HttpResponseUtil
import org.joda.time.DateTime

object UserTagController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def get(userTagId: Int) = IsAuthenticated{ username => implicit request =>
    //logger.info("in UserTagController.get(${userTagId})")
    println("in UserTagController.get(${userTagId})")
    Ok("")
  }
  
  def getAll = IsAuthenticated{ username => implicit request =>
    //logger.info("in UserTagController.getAll()")
    println("in UserTagController.getAll()")
    
    var list = UserTagRepository.findAll(userId)
    val text = Json.toJson(list)
    Ok(text).as(JSON)
  }

  def create = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in UserTagController.create()")
      println("in UserTagController.create()")
      val jsonObj = request.body.asInstanceOf[JsObject]
      // merge userId with the request object
      val userTagObj = Json.obj("userId" -> userId) ++ Json.obj("createdUserId" -> userId) ++
                     Json.obj("createdAt" -> new DateTime()) ++ jsonObj
      userTagObj.validate[UserTag].fold(
            valid = { userTag =>
                    UserTagRepository.create(userTag)
                    Ok(HttpResponseUtil.success("Successfully created tag!"))
            },
            invalid = {
                errors => BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
            }
      )
  }

  def update(userTagId: Int) = IsAuthenticated(parse.json){ username => implicit request =>
    //logger.info(s"in UserTagController.update(${userTagId})")
    println(s"in UserTagController.update(${userTagId})")
    val json = request.body.asInstanceOf[JsObject]
    json.validate[UserTag].fold(
          valid = { UserTag =>
                  UserTagRepository.udate(UserTag, userId)
                  Ok(HttpResponseUtil.success("Successfully updated!"))
          },
          invalid = {
              errors => BadRequest(HttpResponseUtil.error("Something is wrong, please try again!"))
          }
    )
  }
  
  def delete(userTagId: Int) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in UserTagController.delete(${userTagId})")
    println(s"in UserTagController.delete(${userTagId})")
    // first delete all the document tags
    DocumentTagRepository.deleteByUserTagId(userId, userTagId)
    UserTagRepository.delete(userTagId)
    Ok(HttpResponseUtil.success("Successfully deleted!"))
  }

}