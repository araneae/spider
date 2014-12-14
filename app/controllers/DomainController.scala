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

object DomainController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def create = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in DomainController.create...")
      println("in DomainController.create...")
      val jsonObj = request.body.asInstanceOf[JsObject]
      jsonObj.validate[DomainDTO].fold(
          valid = { domainDTO =>
              val domain = Domain(None, domainDTO.industryId, domainDTO.name, domainDTO.description, userId)
              DomainRepository.create(domain)
              Ok(HttpResponseUtil.success("Created domain!"))
          },
          invalid = {  
            errors =>
              println(errors)
              BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
          })
  }
  
  def update(domainId: Long) = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in DomainController.update...")
      println(s"in DomainController.update($domainId)...")
      val jsonObj = request.body.asInstanceOf[JsObject]
      val optDomain = getObject(jsonObj, userId)
      optDomain match {
        case Some(domain) =>
              DomainRepository.update(domain, userId)
              Ok(HttpResponseUtil.success("Updated domain!"))
         case None =>
              Ok(HttpResponseUtil.error("Unable to parse payload!"))
      }
  }
  
  def delete(domainId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in DomainController.delete...")
      println("in DomainController.delete(${domainId})")
      DomainRepository.delete(domainId);
      Ok("Deleted")
  }
  
  def getAll = IsAuthenticated{ username => implicit request =>
      //logger.info("in DomainController.getAll...")
      println("in DomainController.getAll...")
      var list = DomainRepository.findAll
      val text = Json.toJson(list)
      Ok(text).as(JSON)
  }
  
  def get(domainId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in DomainController.get...")
      println("in DomainController.get(${domainId})")
      var optDomain = DomainRepository.find(domainId)
      val text = Json.toJson(optDomain)
      Ok(text).as(JSON)
  }
  
  def getObject(jsonObj: JsObject, userId: Long): Option[Domain] = {
    val optDomainId = (jsonObj \ "domainId").asOpt[Long]
    val optIndustryId = (jsonObj \ "industryId").asOpt[Long]
    val optName = (jsonObj \ "name").asOpt[String]
    val optDescrition = (jsonObj \ "description").asOpt[String]
    
    optName.map { name =>
       optDescrition.map { description =>
          optIndustryId.map { industryId =>
            val domain = Domain(optDomainId, industryId, name, description, userId)
            Some(domain)
          }.getOrElse(None)
       }.getOrElse(None)
    }.getOrElse(None)
  }
}