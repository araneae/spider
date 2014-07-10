package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import play.api.libs.json._
import play.api.mvc.Controller
import traits.Secured
import play.api.libs.json._
import models.tables._
import models.repositories._
import models.dtos._

object SkillController extends Controller with Secured {
  
  private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def create = IsAuthenticated(parse.json){ username => implicit request =>
      logger.info("in SkillController.create...")
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
  
  def update(id: Int) = IsAuthenticated(parse.json){ username => implicit request =>
      logger.info("in SkillController.update...")
      println("in SkillController.update...")
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
  
  def delete(id: Int) = IsAuthenticated{ username => implicit request =>
      logger.info("in SkillController.delete...")
      println("in SkillController.delete...")
      SkillRepository.delete(id);
      Ok("Deleted")
  }
  
  def getAll = IsAuthenticated{ username => implicit request =>
      logger.info("in SkillController.getAll...")
      println("in SkillController.getAll...")
      var list = SkillRepository.findAll
      val text = Json.toJson(list)
      Ok(text).as(JSON)
  }
}