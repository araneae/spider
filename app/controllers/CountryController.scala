package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import play.api.mvc.Controller
import traits.Secured
import play.api.libs.json._
import models.tables._
import models.dtos._
import models.repositories._

object CountryController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def create = IsAuthenticated(parse.json) { username => implicit request =>
      //logger.info("in CountryController.create...")
      println("in CountryController.create...")
      val json = request.body.asInstanceOf[JsObject]
      json.validate[Country].fold(
            valid = { domain =>
                    CountryRepository.create(domain)
                    Ok("Created Country")
            },
            invalid = {
                errors => BadRequest(JsError.toFlatJson(errors))
            }
      )
  }
  
  def getAll = IsAuthenticated { username => implicit request =>
      //logger.info("in CountryController.getAll(...)")
      println("in CountryController.getAll()")
      var list = CountryRepository.findAll
      val text = Json.toJson(list)
      Ok(text).as(JSON)
  }
}