package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import play.api.libs.json._
import play.api.mvc.Controller
import traits.Secured
import models.tables._
import models.dtos._
import models.repositories._

object IndustryController extends Controller with Secured {
  
  private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def create = IsAuthenticated(parse.json){ username => implicit request =>
      logger.info("in IndustryController.create...")
      println("in IndustryController.create...")
      val json = request.body.asInstanceOf[JsObject]
      json.validate[Industry].fold(
            valid = { industry =>
                    IndustryRepository.create(industry)
                    Ok("Created")
            },
            invalid = {
                errors => BadRequest(JsError.toFlatJson(errors))
            }
      )
  }
  
  def update(id: Int) = IsAuthenticated(parse.json){ username => implicit request =>
      logger.info("in IndustryController.update...")
      println("in IndustryController.update...")
      val json = request.body.asInstanceOf[JsObject]
      json.validate[Industry].fold(
            valid = { industry =>
                    IndustryRepository.udate(industry)
                    Ok("Updated")
            },
            invalid = {
                errors => BadRequest(JsError.toFlatJson(errors))
            }
      )
  }
  
  def delete(id: Int) = IsAuthenticated{ username => implicit request =>
      logger.info("in IndustryController.delete...")
      println("in IndustryController.delete...")
      IndustryRepository.delete(id);
      Ok("Deleted")
  }
  
  def getAll = IsAuthenticated{ username => implicit request =>
      logger.info("in IndustryController.get...")
      println("in IndustryController.get...")
      var list = IndustryRepository.findAll
      val text = Json.toJson(list)
      Ok(text).as(JSON)
  }
}