package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import play.api.mvc.Controller
import org.joda.time.DateTime
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
      val optCompany = CompanyRepository.findByUserId(userId)
      optCompany match {
        case Some(company) =>
          val jsonObj = request.body.asInstanceOf[JsObject]
          val jobTitleJsonObj = jsonObj ++ Json.obj("companyId" -> company.companyId)
          jobTitleJsonObj.validate[JobTitleDTO].fold(
                valid = { jobTitleDTO =>
                    val jobTitle = JobTitle(jobTitleDTO, userId, new DateTime, None, None)
                    JobTitleRepository.create(jobTitle)
                    Ok(HttpResponseUtil.success("Successfully created Job Title!"))
                },
                invalid = {  
                  errors =>
                    BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
                }
              )
        case None =>
              BadRequest(HttpResponseUtil.error("Need to upgrade to be able to define Job Title!"))
      }
  }
  
  def update(jobTitleId: Long) = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in JobTitleController.update...")
      println(s"in JobTitleController.update($jobTitleId)...")
      val jsonObj = request.body.asInstanceOf[JsObject]
      jsonObj.validate[JobTitleDTO].fold(
          valid = { jobTitleDTO =>
              val optJobTitle = JobTitleRepository.find(jobTitleId);
              optJobTitle match {
                case Some(jobTitle) =>
                  val updatedJobTitle = JobTitle(jobTitleDTO, jobTitle.createdUserId, jobTitle.createdAt, Some(userId), Some(new DateTime))
                  JobTitleRepository.update(jobTitle)
                  Ok(HttpResponseUtil.success("Successfully updated Job Title!"))
                case None =>
                  BadRequest(HttpResponseUtil.error("Unable to find Job Title!"))
              }
          },
          invalid = {  
            errors =>
              BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
          }
        )
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
      val optCompany = CompanyRepository.findByUserId(userId)
      optCompany match {
        case Some(company) =>
          var list = JobTitleRepository.findAll(company.companyId.get)
          val data = Json.toJson(list)
          Ok(data).as(JSON)
        case None =>
          BadRequest(HttpResponseUtil.error("Need to upgrade to be able to fetch Job Titles!"))
      }
  }
  
  def get(jobTitleId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in JobTitleController.get...")
      println("in JobTitleController.get(${jobTitleId})")
      var optJobTitle = JobTitleRepository.find(jobTitleId)
      val data = Json.toJson(optJobTitle)
      Ok(data).as(JSON)
  }
  
  def getObject(jsonObj: JsObject, userId: Long): Option[JobTitle] = {
    val optJobTitleId = (jsonObj \ "jobTitleId").asOpt[Long]
    val optIndustryId = (jsonObj \ "industryId").asOpt[Long]
    val optName = (jsonObj \ "name").asOpt[String]
    val optDescrition = (jsonObj \ "description").asOpt[String]
    
    optName.map { name =>
       optDescrition.map { description =>
          optIndustryId.map { industryId =>
            val jobTitle = JobTitle(optJobTitleId, industryId, name, description, userId)
            Some(jobTitle)
          }.getOrElse(None)
       }.getOrElse(None)
    }.getOrElse(None)
  }
}