package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import play.api.mvc.Controller
import traits.Secured
import play.api.libs.json._
import models.tables._
import models.dtos._
import org.joda.time.DateTime
import models.repositories._
import utils._

object CompanyController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def get = IsAuthenticated { username => implicit request =>
      //logger.info("in CompanyController.get")
      println("in CompanyController.get")
      val optCompany = CompanyRepository.findByUserId(userId)
      optCompany match {
        case Some(company) => 
              val companyDTO = CompanyDTO(company)
              val text = Json.toJson(companyDTO)
              Ok(text).as(JSON)
        case None =>
            BadRequest(HttpResponseUtil.error("Unable to find company"))
      }
  }
  
  def save = IsAuthenticated(parse.json) { username => implicit request =>
      //logger.info("in CompanyController.save")
      println("in CompanyController.save")
      val jsonObj = request.body.asInstanceOf[JsObject]
      jsonObj.validate[CompanyDTO].fold(
            valid = { companyDTO =>
                  companyDTO.companyId match {
                    case Some(companyId) =>
                      val optCompany = CompanyRepository.find(companyId)
                      optCompany match {
                        case Some(company) =>
                            val updatedCompany = Company(companyDTO,
                                                        company.createdUserId,
                                                        company.createdAt,
                                                        Some(userId),
                                                        Some(new DateTime()))
                              CompanyRepository.update(updatedCompany)
                              Ok(HttpResponseUtil.success("Successfull updated company settings."))
                        case None =>
                          Ok(HttpResponseUtil.error("Unable to find company"))
                      }
                    case None =>
                      val company = Company(companyDTO, userId, new DateTime, None, None)
                    CompanyRepository.create(company)
                    Ok(HttpResponseUtil.success("Successfull setup company."))
                  }
            },
            invalid = {
                errors =>
                    BadRequest(HttpResponseUtil.error("Unable to parse payload."))
            }
      )
  }
}