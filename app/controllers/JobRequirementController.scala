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

object JobRequirementController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def create = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in JobRequirementController.create...")
      println("in JobRequirementController.create...")
      val jsonObj = request.body.asInstanceOf[JsObject]
      val optJobRequirement = getObject(jsonObj, userId)
      optJobRequirement match {
        case Some(jobRequirement) =>
              JobRequirementRepository.create(jobRequirement)
              Ok(HttpResponseUtil.success("Created jobRequirement!"))
        case None =>
              Ok(HttpResponseUtil.error("Unable to parse payload!"))
      }
  }
  
  def update(jobRequirementId: Long) = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in JobRequirementController.update...")
      println(s"in JobRequirementController.update($jobRequirementId)...")
      val jsonObj = request.body.asInstanceOf[JsObject]
      val optJobRequirement = getObject(jsonObj, userId)
      optJobRequirement match {
        case Some(jobRequirement) =>
              JobRequirementRepository.update(jobRequirement, userId)
              Ok(HttpResponseUtil.success("Updated jobRequirement!"))
         case None =>
              Ok(HttpResponseUtil.error("Unable to parse payload!"))
      }
  }
  
  def delete(jobRequirementId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in JobRequirementController.delete...")
      println("in JobRequirementController.delete(${jobRequirementId})")
      JobRequirementRepository.delete(jobRequirementId);
      Ok("Deleted")
  }
  
  def getAll = IsAuthenticated{ username => implicit request =>
      //logger.info("in JobRequirementController.getAll...")
      println("in JobRequirementController.getAll...")
      var list = JobRequirementRepository.findAll
      val text = Json.toJson(list)
      Ok(text).as(JSON)
  }
  
  def get(jobRequirementId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in JobRequirementController.get...")
      println("in JobRequirementController.get(${jobRequirementId})")
      var optJobRequirement = JobRequirementRepository.find(jobRequirementId)
      val text = Json.toJson(optJobRequirement)
      Ok(text).as(JSON)
  }
  
  def getObject(jsonObj: JsObject, userId: Long): Option[JobRequirement] = {
    val optJobRequirementId = (jsonObj \ "jobRequirementId").asOpt[Long]
    val optIndustryId = (jsonObj \ "industryId").asOpt[Long]
    val optName = (jsonObj \ "name").asOpt[String]
    val optCode = (jsonObj \ "code").asOpt[String]
    val optDescrition = (jsonObj \ "description").asOpt[String]
    
    optName.map { name =>
       optCode.map { code =>
         optDescrition.map { description =>
            optIndustryId.map { industryId =>
              //val jobRequirement = JobRequirement(optJobRequirementId, industryId, code, name, Some(description), userId)
              //Some(jobRequirement)
              None
            }.getOrElse(None)
         }.getOrElse(None)
       }.getOrElse(None)
    }.getOrElse(None)
  }
}