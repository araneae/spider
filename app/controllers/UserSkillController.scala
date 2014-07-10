package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import play.api.libs.json._
import play.api.mvc.Controller
import traits.Secured
import scala.collection.mutable.ArrayBuffer
import models.repositories._
import models.tables._
import enums.SkillLevel
import models.dtos._

object UserSkillController extends Controller with Secured {
  
  private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def getAll = IsAuthenticated{ username => implicit request =>
      logger.info("in SkillController.getAll...")
      println("in SkillController.getAll...")
      var list = UserSkillRepository.findAll(userId)
      val text = Json.toJson(list)
      Ok(text).as(JSON)
  }
  
  def get(skillId: Int) = IsAuthenticated{ username => implicit request =>
      logger.info("in SkillController.get(${skillId})...")
      println("in SkillController.get(${skillId})...")
      var skill = UserSkillRepository.find(userId, skillId)
      val text = Json.toJson(skill)
      Ok(text).as(JSON)
  }
  
  def create(skillId: Int) = IsAuthenticated(parse.json){ username => implicit request =>
      logger.info("in SkillController.create...")
      println("in SkillController.create...")
      val jsonObj = request.body.asInstanceOf[JsObject]
      // merge userId with the request object
      val userIdObj = Json.obj("userId" -> userId)
      val userSkillObj = userIdObj ++ jsonObj
      userSkillObj.validate[UserSkill].fold(
            valid = { skill =>
                    UserSkillRepository.create(skill)
                    Ok("Created")
            },
            invalid = {
                errors => BadRequest(JsError.toFlatJson(errors))
            }
      )
  }
  
  def update(skillId: Int) = IsAuthenticated(parse.json){ username => implicit request =>
      logger.info("in SkillController.update...")
      println("in SkillController.update...")
      val json = request.body.asInstanceOf[JsObject]
      println(s"json ${json}")
      json.validate[UserSkill].fold(
            valid = { skill =>
                    UserSkillRepository.udate(skill)
                    Ok("Updated")
            },
            invalid = {
                errors => BadRequest(JsError.toFlatJson(errors))
            }
      )
  }
  
  def delete(skillId: Int) = IsAuthenticated{ username => implicit request =>
      logger.info("in SkillController.delete...")
      println("in deleteMySkill...")
      UserSkillRepository.delete(userId, skillId);
      Ok("Deleted")
  }
  
  def getSkillLevels = IsAuthenticated{ username => implicit request =>
      logger.info("in SkillController.getSkillLevels...")
      println("in getSkillLevels...")
      
      var b= new StringBuilder
      for (d <- SkillLevel.values){
        val item = new StringContext(",{ \"name\":\"", "\"}").s(d.toString())
        b ++= item
      }
      b.deleteCharAt(0);
      b.insert(0, "[")
      b ++= "]";
      Ok(b.toString()).as(JSON)
  }
  
}