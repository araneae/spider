package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import play.api.libs.json._
import play.api.mvc.{Action, Controller}
import be.objectify.deadbolt.scala.DeadboltActions
import traits.Secured
import org.joda.time.DateTime
import utils.HttpResponseUtil
import models.tables._
import models.repositories._
import models.dtos._
import security._

object PermissionController extends Controller with DeadboltActions with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def create = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in PermissionController.create")
      println("in PermissionController.create")
      val json = request.body.asInstanceOf[JsObject]
      json.validate[PermissionDTO].fold(
            valid = { permissionDTO =>
                    PermissionRepository.create(Permission(permissionDTO, userId, new DateTime, None, None))
                    Ok(HttpResponseUtil.success("Successfully created permission!"))
            },
            invalid = {
                errors => BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
            }
      )
  }
  
  def update(permissionId: Long) = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in PermissionController.update(${permissionId})")
      println("in PermissionController.update(${permissionId})")
      val json = request.body.asInstanceOf[JsObject]
      json.validate[PermissionDTO].fold(
            valid = { permissionDTO =>
                    val optPermission = PermissionRepository.find(permissionDTO.permissionId.get)
                    optPermission match {
                      case Some(permission) => 
                            val updatedPermission = Permission(permissionDTO, permission.createdUserId, 
                                                              permission.createdAt, Some(userId), Some(new DateTime))
                            PermissionRepository.update(updatedPermission)
                            Ok(HttpResponseUtil.success("Successfully updated permission!"))
                      case None =>
                            BadRequest(HttpResponseUtil.error("Unable to find permission!"))
                    } 
            },
            invalid = {
                errors => BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
            }
      )
  }
  
  def delete(permissionId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in PermissionController.delete(${permissionId})")
      println("in PermissionController.delete(${permissionId})")
      PermissionRepository.delete(permissionId);
      Ok(HttpResponseUtil.success("Successfully deleted permission!"))
  }
  
  def get(permissionId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in PermissionController.get(${permissionId})")
      println("in PermissionController.get(${permissionId})")
      var optPermission = PermissionRepository.get(permissionId)
      val data = Json.toJson(optPermission)
      Ok(data).as(JSON)
  }
  
  def getAll = IsAuthenticated{ username => implicit request =>
      //logger.info("in PermissionController.getAll...")
      println("in PermissionController.getAll")
      var list = PermissionRepository.getAll
      val data = Json.toJson(list)
      Ok(data).as(JSON)
  }
  
  def restrictOne = Restrict(Array("foo"), new MyDeadboltHandler) {
                      Action {
                        Ok("")
                      }
                    }
}