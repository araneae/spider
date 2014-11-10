package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import play.api.libs.json._
import play.api.mvc.Controller
import traits.Secured
import models.tables._
import models.dtos._
import models.repositories._
import utils.HttpResponseUtil

object IndustryController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def create = IsAuthenticated(parse.json){ username => implicit request =>
    //logger.info("in IndustryController.create...")
    println("in IndustryController.create...")
    val jsonObj = request.body.asInstanceOf[JsObject]
    val optIndustryObj = getObject(jsonObj, userId)
    optIndustryObj.map{ industry =>
      IndustryRepository.create(industry)
      Ok(HttpResponseUtil.success("Created Industry!"))
    }.getOrElse(
      Ok(HttpResponseUtil.error("Unable to parse payload!"))
    )
  }
  
  def update(industryId: Long) = IsAuthenticated(parse.json){ username => implicit request =>
    //logger.info("in IndustryController.update...")
    println("in IndustryController.update(${industryId})")
    val jsonObj = request.body.asInstanceOf[JsObject]
    val optIndustryObj = getObject(jsonObj, userId)
    println("optIndustryObj "+optIndustryObj)
    optIndustryObj.map{ industry =>
      IndustryRepository.udate(industry, userId)
      Ok(HttpResponseUtil.success("Updated Industry!"))
    }.getOrElse(
       Ok(HttpResponseUtil.error("Unable to parse payload!"))
    )
  }
  
  def delete(industryId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in IndustryController.delete...")
      println("in IndustryController.delete...")
      IndustryRepository.delete(industryId);
      Ok("Deleted")
  }
  
  def getAll = IsAuthenticated{ username => implicit request =>
      //logger.info("in IndustryController.getAll()")
      println("in IndustryController.getAll()")
      var list = IndustryRepository.findAll
      val text = Json.toJson(list)
      Ok(text).as(JSON)
  }
  
  def get(industryId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in IndustryController.get...")
      println("in IndustryController.get(${industryId})")
      var optIndustry = IndustryRepository.find(industryId)
      val text = Json.toJson(optIndustry)
      Ok(text).as(JSON)
  }
  
  def getObject(jsonObj: JsObject, userId: Long): Option[Industry] = {
    val optIndustryId = (jsonObj \ "industryId").asOpt[Long]
    val optName = (jsonObj \ "name").asOpt[String]
    val optCode = (jsonObj \ "code").asOpt[String]
    val optDescrition = (jsonObj \ "description").asOpt[String]
    
    optName.map { name =>
       optCode.map { code =>
         optDescrition.map{ description =>
           val industry = Industry(optIndustryId, code, name, Some(description), userId)
           Some(industry)
         }.getOrElse(None)
       }.getOrElse(None)
    }.getOrElse(None)
  }
}