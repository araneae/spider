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

object SubscriptionPermissionController extends Controller with DeadboltActions with Secured {
  
  def create(subscriptionId: Long, permissionId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in SubscriptionPermissionController.create")
      println(s"in SubscriptionPermissionController.create, ${subscriptionId},${permissionId}")
      val subscriptionPermission = SubscriptionPermission (subscriptionId,permissionId, userId, new DateTime, None, None)  
      SubscriptionPermissionRepository.create(subscriptionPermission)
      Ok(HttpResponseUtil.success("Successfully created subscription!"))
  }
  
  def delete(subscriptionId: Long, permissionId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in SubscriptionPermissionController.delete(${subscriptionId,permissionId})")
      println(s"in SubscriptionPermissionController.delete(${subscriptionId},${permissionId})")
      SubscriptionPermissionRepository.delete(subscriptionId,permissionId);
      Ok(HttpResponseUtil.success("Successfully deleted subscription permission mapping!"))
  }
  
  def get(subscriptionId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in SubscriptionPermissionController.get(${subscriptionId})")
      println(s"in SubscriptionPermissionController.get(${subscriptionId})")
      var optSubscription = SubscriptionPermissionRepository.get(subscriptionId)
      val data = Json.toJson(optSubscription)
      Ok(data).as(JSON)
  }
  
  def getAll = IsAuthenticated{ username => implicit request =>
      //logger.info("in SubscriptionPermissionController.get())
      println(s"in SubscriptionPermissionController.getAll()")
      var optSubscription = SubscriptionPermissionRepository.getAll()
      val data = Json.toJson(optSubscription)
      Ok(data).as(JSON)
  }
}