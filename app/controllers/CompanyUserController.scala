package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import play.api.libs.json._
import play.api.mvc.Controller
import org.joda.time.DateTime
import traits.Secured
import models.tables._
import models.repositories._
import models.dtos._
import utils._

object CompanyUserController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def create = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in CompanyUserController.create")
      println("in CompanyUserController.create")
      val json = request.body.asInstanceOf[JsObject]
      json.validate[CompanyUserDTO].fold(
            valid = { companyUserDTO =>
                    val optCompany = CompanyRepository.findByUserId(userId)
                    optCompany match {
                      case Some(company) => 
                          val companyUser = CompanyUser(companyUserDTO, userId, new DateTime, None, None)
                          CompanyUserRepository.create(companyUser)
                          Ok(HttpResponseUtil.success("Successfully created company user."))
                      case None =>
                          BadRequest(HttpResponseUtil.error("Unable to find company."))
                    }
            },
            invalid = {
                errors =>
                  BadRequest(JsError.toFlatJson(errors))
            }
      )
  }
  
  def update(companyUserId: Long) = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in CompanyUserController.update(${companyUserId})")
      println("in CompanyUserController.update(${companyUserId})")
      val json = request.body.asInstanceOf[JsObject]
      json.validate[CompanyUserDTO].fold(
            valid = { companyUserDTO =>
                    val optCompanyUser = CompanyUserRepository.find(companyUserDTO.companyUserId.get)
                    optCompanyUser match {
                      case Some(companyUser) =>
                        val updatedCompanyUser = CompanyUser(companyUserDTO, 
                                                             companyUser.createdUserId, 
                                                             companyUser.createdAt,
                                                             Some(userId),
                                                             Some(new DateTime))
                        CompanyUserRepository.update(updatedCompanyUser)
                        Ok(HttpResponseUtil.success("Successfully updated company user."))
                      case None =>
                        BadRequest(HttpResponseUtil.error("Unable to find company user."))
                    }
            },
            invalid = {
                errors => BadRequest(HttpResponseUtil.error("Unable to parse payload."))
            }
      )
  }
  
  def delete(companyUserId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in CompanyUserController.delete(${companyUserId})")
      println("in CompanyUserController.delete(${companyUserId})")
      CompanyUserRepository.delete(companyUserId);
      Ok(HttpResponseUtil.success("Successfully deleted the company user."))
  }
  
  def getAll = IsAuthenticated{ username => implicit request =>
      //logger.info("in CompanyUserController.getAll")
      println("in CompanyUserController.getAll")
      val optCompany = CompanyRepository.findByUserId(userId)
      optCompany match {
        case Some(company) => 
            var list = CompanyUserRepository.findAll(company.companyId.get)
            val text = Json.toJson(list)
            Ok(text).as(JSON)
        case None =>
            BadRequest(HttpResponseUtil.error("Unable to find company."))
      }
  }
  
  def get(companyUserId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info(s"in CompanyUserController.get(${companyUserId})")
      println(s"in CompanyUserController.get(${companyUserId})")
      var optCompanyUser = CompanyUserRepository.find(companyUserId)
      optCompanyUser match {
        case Some(companyUser) =>
            val companyUserDTO = CompanyUserDTO(companyUser)
            val text = Json.toJson(companyUserDTO)
            Ok(text).as(JSON)
        case None =>
            BadRequest(HttpResponseUtil.error("Unable to find company user."))
      }
  }
}