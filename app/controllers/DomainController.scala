package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import play.api.mvc.Controller
import traits.Secured
import play.api.libs.json._
import models.tables._
import models.dtos._
import models.repositories._

object DomainController extends Controller with Secured {
  
  private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def create = IsAuthenticated(parse.json){ username => implicit request =>
      logger.info("in DomainController.create...")
      println("in DomainController.create...")
      val json = request.body.asInstanceOf[JsObject]
      json.validate[Domain].fold(
            valid = { domain =>
                    DomainRepository.create(domain)
                    Ok("Created")
            },
            invalid = {
                errors => BadRequest(JsError.toFlatJson(errors))
            }
      )
  }
  
  def update(id: Int) = IsAuthenticated(parse.json){ username => implicit request =>
      logger.info("in DomainController.update...")
      println(s"in DomainController.update($id)...")
      val json = request.body.asInstanceOf[JsObject]
      json.validate[Domain].fold(
            valid = { domain =>
                    DomainRepository.udate(domain)
                    Ok("Updated")
            },
            invalid = {
                errors => BadRequest(JsError.toFlatJson(errors))
            }
      )
  }
  
  def delete(id: Int) = IsAuthenticated{ username => implicit request =>
      logger.info("in DomainController.delete...")
      println("in DomainController.delete...")
      DomainRepository.delete(id);
      Ok("Deleted")
  }
  
  def getAll = IsAuthenticated{ username => implicit request =>
      logger.info("in DomainController.getAll...")
      println("in DomainController.getAll...")
      var list = DomainRepository.findAll
      val text = Json.toJson(list)
      Ok(text).as(JSON)
  }
}