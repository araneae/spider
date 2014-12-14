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

object JobRequirementController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def create = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in JobRequirementController.create...")
      println("in JobRequirementController.create...")
      val optCompany = CompanyRepository.findByUserId(userId)
      optCompany match {
        case Some(company) =>
          val jsonObj = request.body.asInstanceOf[JsObject]
          val jobReqJsonObj = jsonObj ++ Json.obj("companyId" -> company.companyId)
          jobReqJsonObj.validate[JobRequirementDTO].fold(
                valid = { jobRequirementDTO =>
                    val jobRequirementXtn = JobRequirementXtn(jobRequirementDTO.xtn, userId, new DateTime, None, None)
                    val jobRequirementXtnId = JobRequirementXtnRepository.create(jobRequirementXtn)
                    val jobRequirement = JobRequirement(jobRequirementDTO, jobRequirementXtnId, userId, new DateTime, None, None)
                    JobRequirementRepository.create(jobRequirement)
                    Ok(HttpResponseUtil.success("Successfully created Job Requirement!"))
                },
                invalid = {  
                  errors =>
                    BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
                }
              )
        case None =>
              BadRequest(HttpResponseUtil.error("Need to upgrade to be able to define Job Requirement!"))
      }
  }
  
  def update(jobRequirementId: Long) = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in JobRequirementController.update...")
      println(s"in JobRequirementController.update(${jobRequirementId})")
      val jsonObj = request.body.asInstanceOf[JsObject]
      jsonObj.validate[JobRequirementDTO].fold(
            valid = { jobRequirementDTO =>
              val optJobRequirement = JobRequirementRepository.find(jobRequirementId)
              optJobRequirement match {
                case Some(jobRequirement) =>
                    val updatedJobRequirement = JobRequirement(jobRequirementDTO,
                                                               jobRequirementDTO.xtn.jobRequirementXtnId.get,
                                                               jobRequirement.createdUserId, 
                                                               jobRequirement.createdAt, 
                                                               Some(userId), 
                                                               Some(new DateTime))
                    JobRequirementRepository.update(updatedJobRequirement)
                    val optJobRequirementXtn = JobRequirementXtnRepository.find(jobRequirementDTO.xtn.jobRequirementXtnId.get)
                    optJobRequirementXtn match {
                      case Some(jobRequirementXtn) =>
                         val updatedJobRequirementXtn = JobRequirementXtn(jobRequirementDTO.xtn,
                                                               jobRequirementXtn.createdUserId, 
                                                               jobRequirementXtn.createdAt, 
                                                               Some(userId), 
                                                               Some(new DateTime))
                        JobRequirementXtnRepository.update(updatedJobRequirementXtn)
                    }
                    Ok(HttpResponseUtil.success("Successfully updated JobRequirement!"))
                 case None =>
                    BadRequest(HttpResponseUtil.error("Unable to find job requirment!"))
              }
            },
            invalid = {
                errors =>
                  BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
            }
      )
  }
  
  def delete(jobRequirementId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in JobRequirementController.delete...")
      println(s"in JobRequirementController.delete(${jobRequirementId})")
      var optJobRequirement = JobRequirementRepository.find(jobRequirementId)
      optJobRequirement match {
        case Some(jobRequirement) =>
                JobRequirementRepository.delete(jobRequirementId)
                JobRequirementXtnRepository.delete(jobRequirement.jobRequirementXtnId)
                Ok(HttpResponseUtil.success("Successfully deleted Job Requirement!"))
        case None =>
          BadRequest(HttpResponseUtil.error("Unable to find Job Requirement!"))
      }
  }
  
  def getAll = IsAuthenticated{ username => implicit request =>
      //logger.info("in JobRequirementController.getAll...")
      println("in JobRequirementController.getAll...")
      val optCompany = CompanyRepository.findByUserId(userId)
      optCompany match {
        case Some(company) =>
          var list = JobRequirementRepository.getAll(company.companyId.get)
          val data = Json.toJson(list)
          Ok(data).as(JSON)
        case None =>
          BadRequest(HttpResponseUtil.error("Need to upgrade to be able to define Job Requirement!"))
      }
  }
  
  def get(jobRequirementId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in JobRequirementController.get...")
      println("in JobRequirementController.get(${jobRequirementId})")
      var optJobRequirementDTO = JobRequirementRepository.get(jobRequirementId)
      val data = Json.toJson(optJobRequirementDTO)
      Ok(data).as(JSON)
  }
}