package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import play.api.mvc.Controller
import org.joda.time.DateTime
import traits.Secured
import actors._
import traits._
import akka.pattern.ask
import akka.util.Timeout
import java.util.concurrent.TimeUnit
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Await
import play.api.libs.json._
import models.tables._
import models.dtos._
import models.repositories._
import utils.HttpResponseUtil
import enums.JobStatusType

object JobRequirementController extends Controller with Secured with AkkaActor {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def create = IsAuthorized(parse.json)("job.create"){ username => implicit request =>
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
                    val jobRequirementId = JobRequirementRepository.create(jobRequirement)
                    
                    // update lucene index
                    updateIndex(company, jobRequirementId, jobRequirementXtnId)
                    
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
  
  def update(jobRequirementId: Long) = IsAuthorized(parse.json)("job.edit"){ username => implicit request =>
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
                        
                        // update lucene index
                        val optCompany = CompanyRepository.find(jobRequirement.companyId)
                        optCompany match {
                           case Some(company) =>
                             updateIndex(company, jobRequirementId, jobRequirementXtn.jobRequirementXtnId.get)
                           case None =>
                        }
                    
                        Ok(HttpResponseUtil.success("Successfully updated JobRequirement!"))
                      case None =>
                        BadRequest(HttpResponseUtil.error("Unable to find job requirment xtn!"))
                    }
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
  
  def post(jobRequirementId: Long) = IsAuthorized("job.post"){ username => implicit request =>
      //logger.info(s"in JobRequirementController.post(${jobRequirementId})")
      println(s"in JobRequirementController.post(${jobRequirementId})")

      val optJobRequirement = JobRequirementRepository.find(jobRequirementId)
      optJobRequirement match {
        case Some(jobRequirement) =>
                JobRequirementRepository.updateStatus(jobRequirementId, userId, JobStatusType.POSTED)
                
                // update lucene index
                val optCompany = CompanyRepository.find(jobRequirement.companyId)
                optCompany match {
                   case Some(company) =>
                     updateIndex(company, jobRequirementId, jobRequirement.jobRequirementXtnId)
                   case None =>
                }
            
                Ok(HttpResponseUtil.success("Successfully posted Job!"))
         case None =>
               BadRequest(HttpResponseUtil.error("Unable to find job!"))
      }
  }
  
  def makeDraft(jobRequirementId: Long) = IsAuthorized("job.edit"){ username => implicit request =>
      //logger.info(s"in JobRequirementController.makeDraft(${jobRequirementId})")
      println(s"in JobRequirementController.makeDraft(${jobRequirementId})")

      val optJobRequirement = JobRequirementRepository.find(jobRequirementId)
      optJobRequirement match {
        case Some(jobRequirement) =>
                JobRequirementRepository.updateStatus(jobRequirementId, userId, JobStatusType.DRAFT)
                
                // update lucene index
                val optCompany = CompanyRepository.find(jobRequirement.companyId)
                optCompany match {
                   case Some(company) =>
                     updateIndex(company, jobRequirementId, jobRequirement.jobRequirementXtnId)
                   case None =>
                }
            
                Ok(HttpResponseUtil.success("Successfully changed status to draft!"))
         case None =>
               BadRequest(HttpResponseUtil.error("Unable to find job!"))
      }
  }
  
  def delete(jobRequirementId: Long) = IsAuthorized("job.delete"){ username => implicit request =>
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
      //logger.info(s"in JobRequirementController.get(${jobRequirementId})")
      println(s"in JobRequirementController.get(${jobRequirementId})")
      var optJobRequirementDTO = JobRequirementRepository.get(jobRequirementId)
      val data = Json.toJson(optJobRequirementDTO)
      Ok(data).as(JSON)
  }
  
  def preview(jobRequirementId: Long) = IsAuthorized("job.preview"){ username => implicit request =>
      //logger.info(s"in JobRequirementController.preview(${jobRequirementId})")
      println(s"in JobRequirementController.preview(${jobRequirementId})")
      var optJobDTO = JobRequirementRepository.getJobDTOByRequirementId(jobRequirementId)
      val data = Json.toJson(optJobDTO)
      Ok(data).as(JSON)
  }
  
  def search = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in JobRequirementController.search())")
      println("in JobRequirementController.search()")
      
      val jsonObj = request.body.asInstanceOf[JsObject]
      jsonObj.validate[JobSearchDTO].fold(
            valid = { jobSearchDTO =>
                implicit val timeout = Timeout(MESSAGE_TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
                // send message to index searcher
                val f = ask(indexSearcherActor, MessageJobSearch(jobSearchDTO)).mapTo[MessageJobSearchResult]
                val result = f.map {
                   case MessageJobSearchResult(jobRequirementIds) => {
                              println(s"jobRequirementIds: ${jobRequirementIds}")
                              var list = JobRequirementRepository.getAllJobDTOByRequirementIds(jobRequirementIds)
                              val data = Json.toJson(list)
                              Ok(data).as(JSON)
                     }
                   case _ => Ok("").as(JSON)
//                case Failure(failure) =>
//                        println(s"Failrure ${failure}")
//                        Ok("")
                }
                Await.result(result, timeout.duration)
            },
            invalid = {  
              errors =>
                println(errors)
                BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
            }
          )
  }
  
  def apply(jobRequirementId: Long) = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in JobRequirementController.apply(${jobRequirementId}))")
      println("in JobRequirementController.apply(${jobRequirementId})")
      
      val jsonObj = request.body.asInstanceOf[JsObject]
      jsonObj.validate[JobApplicationDTO].fold(
            valid = { jobApplicationDTO =>
                      val jobApplication = JobApplication(jobApplicationDTO, userId, new DateTime, None, None)
                      val jobApplicationId = JobApplicationRepository.create(jobApplication)
                      
                      jobApplicationDTO.attachments.map { attachment =>
                                val jobApplicationAttachment = JobApplicationAttachment(attachment, jobApplicationId, userId, new DateTime, None, None)
                                val jobApplicationAttachmentId = JobApplicationAttachmentRepository.create(jobApplicationAttachment)
                      }
                      Ok(HttpResponseUtil.success("Successfully created job application!"))
            },
            invalid = {  
              errors =>
                println(errors)
                BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
            }
          )
  }
  
  def updateIndex(company: Company, jobRequirementId: Long, jobRequirementXtnId: Long) = {
      val optJobRequirement = JobRequirementRepository.find(jobRequirementId)
      val optJobRequirementXtn = JobRequirementXtnRepository.find(jobRequirementXtnId)
      (optJobRequirement, optJobRequirementXtn) match {
        case (Some(savedJobRequirement), Some(savedJobRequirementXtn)) =>
                                indexWriterActor ! MessageAddJob(company, savedJobRequirement, savedJobRequirementXtn)
        case _ =>
      }
  }
}