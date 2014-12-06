package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import play.api.mvc.Controller
import traits.Secured
import play.api.libs.json._
import models.tables._
import models.dtos._
import models.repositories._
import utils.HttpResponseUtil

object JobTitleController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def create = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in JobTitleController.create...")
      println("in JobTitleController.create...")
      val jsonObj = request.body.asInstanceOf[JsObject]
      val optJobTitle = getObject(jsonObj, userId)
      optJobTitle match {
        case Some(jobTitle) =>
              JobTitleRepository.create(jobTitle)
              Ok(HttpResponseUtil.success("Created jobTitle!"))
        case None =>
              Ok(HttpResponseUtil.error("Unable to parse payload!"))
      }
  }
  
  def update(jobTitleId: Long) = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in JobTitleController.update...")
      println(s"in JobTitleController.update($jobTitleId)...")
      val jsonObj = request.body.asInstanceOf[JsObject]
      val optJobTitle = getObject(jsonObj, userId)
      optJobTitle match {
        case Some(jobTitle) =>
              JobTitleRepository.update(jobTitle, userId)
              Ok(HttpResponseUtil.success("Updated jobTitle!"))
         case None =>
              Ok(HttpResponseUtil.error("Unable to parse payload!"))
      }
  }
  
  def delete(jobTitleId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in JobTitleController.delete...")
      println("in JobTitleController.delete(${jobTitleId})")
      JobTitleRepository.delete(jobTitleId);
      Ok("Deleted")
  }
  
  def getAll = IsAuthenticated{ username => implicit request =>
      //logger.info("in JobTitleController.getAll...")
      println("in JobTitleController.getAll...")
      var list = JobTitleRepository.findAll
      val text = Json.toJson(list)
      Ok(text).as(JSON)
  }
  
  def get(jobTitleId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in JobTitleController.get...")
      println("in JobTitleController.get(${jobTitleId})")
      var optJobTitle = JobTitleRepository.find(jobTitleId)
      val text = Json.toJson(optJobTitle)
      Ok(text).as(JSON)
  }
  
  def getObject(jsonObj: JsObject, userId: Long): Option[JobTitle] = {
    val optJobTitleId = (jsonObj \ "jobTitleId").asOpt[Long]
    val optIndustryId = (jsonObj \ "industryId").asOpt[Long]
    val optName = (jsonObj \ "name").asOpt[String]
    val optCode = (jsonObj \ "code").asOpt[String]
    val optDescrition = (jsonObj \ "description").asOpt[String]
    
    optName.map { name =>
       optCode.map { code =>
         optDescrition.map { description =>
            optIndustryId.map { industryId =>
              val jobTitle = JobTitle(optJobTitleId, industryId, code, name, Some(description), userId)
              Some(jobTitle)
            }.getOrElse(None)
         }.getOrElse(None)
       }.getOrElse(None)
    }.getOrElse(None)
  }
}