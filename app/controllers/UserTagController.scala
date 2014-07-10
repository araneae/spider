package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import models.dtos.UserTag
import models.repositories.UserTagRepository
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Controller
import traits.Secured
import utils.HttpResponseUtil

object UserTagController extends Controller with Secured {
  
  private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def get(Id: Int) = IsAuthenticated{ username => implicit request =>
    logger.info("in UserTagController.get...")
    println("in UserTagController.get...")
    Ok("")
  }
  
  def getAll = IsAuthenticated{ username => implicit request =>
    logger.info("in UserTagController.getAll...")
    println("in UserTagController.getAll...")
    
    var list = UserTagRepository.findAll(userId)
    val text = Json.toJson(list)
    Ok(text).as(JSON)
  }

  def create = IsAuthenticated(parse.json){ username => implicit request =>
      logger.info("in UserTagController.create...")
      println("in UserTagController.create...")
      val jsonObj = request.body.asInstanceOf[JsObject]
      // merge userId with the request object
      val userIdObj = Json.obj("userId" -> userId)
      val userTagObj = userIdObj ++ jsonObj
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

  def update(id: Int) = IsAuthenticated{ username => implicit request =>
    logger.info("in UserTagController.update...")
    println("in UserTagController.update...")
    val json = request.body.asInstanceOf[JsObject]
    json.validate[UserTag].fold(
          valid = { UserTag =>
                  UserTagRepository.udate(UserTag)
                  Ok(HttpResponseUtil.success("Successfully updated!"))
          },
          invalid = {
              errors => BadRequest(HttpResponseUtil.error("Something is wrong, please try again!"))
          }
    )
  }
  
  def delete(id: Int) = IsAuthenticated{ username => implicit request =>
    Ok("")
  }

}