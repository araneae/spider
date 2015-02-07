package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import play.api.mvc.Controller
import play.api.libs.json._
import traits.Secured
import models._
import utils._

object UserPermissionController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def getAll = IsAuthenticated { username => implicit request =>
    //logger.info("in UserPermissionController.getAll...")
    println("in UserPermissionController.getAll...")
    val list = List("site.admin") // TBD - Fetch user's permissions
    val data = Json.toJson(list)
    Ok(data).as(JSON)
  }
  
}