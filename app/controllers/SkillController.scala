package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import play.api.libs.json._
import play.api.mvc.Controller
import traits.Secured
import models.tables._
import models.repositories._
import models.dtos._

object SkillController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def create = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in SkillController.create...")
      println("in SkillController.create...")
      val json = request.body.asInstanceOf[JsObject]
      json.validate[Skill].fold(
            valid = { skill =>
                    SkillRepository.create(skill)
                    Ok("Created")
            },
            invalid = {
                errors => BadRequest(JsError.toFlatJson(errors))
            }
      )
  }
  
  def update(skillId: Int) = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in SkillController.update(${skillId})")
      println("in SkillController.update(${skillId})")
      val json = request.body.asInstanceOf[JsObject]
      json.validate[Skill].fold(
            valid = { skill =>
                    SkillRepository.udate(skill)
                    Ok("Updated")
            },
            invalid = {
                errors => BadRequest(JsError.toFlatJson(errors))
            }
      )
  }
  
  def delete(skillId: Int) = IsAuthenticated{ username => implicit request =>
      //logger.info("in SkillController.delete(${skillId})")
      println("in SkillController.delete(${skillId})")
      SkillRepository.delete(skillId);
      Ok("Deleted")
  }
  
  def getAll = IsAuthenticated{ username => implicit request =>
      //logger.info("in SkillController.getAll...")
      println("in SkillController.getAll...")
      var list = SkillRepository.findAll
      val text = Json.toJson(list)
      Ok(text).as(JSON)
  }
}