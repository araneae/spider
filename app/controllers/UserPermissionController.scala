package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import play.api.mvc.Controller
import play.api.libs.json._
import traits.Secured
import services._
import models._
import utils._

object UserPermissionController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def getAll = IsAuthenticated { username => implicit request =>
    //logger.info("in UserPermissionController.getAll...")
    println("in UserPermissionController.getAll...")
    val list = CacheService.getUserPermissions(request)
    val data = Json.toJson(list ::: List("job.create", "job.edit", "job.delete", "job.preview", "job.post"))
    Ok(data).as(JSON)
  }
  
}