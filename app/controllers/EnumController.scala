package controllers

import org.mindrot.jbcrypt.BCrypt
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import models.dtos.User
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick._
import play.api.mvc.Action
import play.api.mvc.Controller
import traits._
import play.api.mvc.Flash
import models.repositories._
import actors._
import utils._
import services._
import play.api.libs.json._
import enums._

object EnumController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def getAll = IsAuthenticated { username => implicit request =>
    //logger.info("in EnumController.getAll()")
    println("in EnumController.getAll()")
    val json = Json.obj("CurrencyType" -> Json.toJson(CurrencyType.values)) ++ 
               Json.obj("EmploymentType" -> Json.toJson(EmploymentType.values)) ++
               Json.obj("SalaryTermType" -> Json.toJson(SalaryTermType.values)) ++
               Json.obj("JobStatusType" -> Json.toJson(JobStatusType.values)) ++
               Json.obj("GenderType" -> Json.toJson(GenderType.values)) ++
               Json.obj("MaritalStatusType" -> Json.toJson(MaritalStatusType.values)) ++
               Json.obj("CompanyUserStatusType" -> Json.toJson(CompanyUserStatusType.values)) ++
               Json.obj("CompanyUserType" -> Json.toJson(CompanyUserType.values));
      Ok(json).as(JSON)
  }
  
}