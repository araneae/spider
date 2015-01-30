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

object SubscriptionController extends Controller with DeadboltActions with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def create = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in SubscriptionController.create")
      println("in SubscriptionController.create")
      val json = request.body.asInstanceOf[JsObject]
      json.validate[SubscriptionDTO].fold(
            valid = { subscriptionDTO =>
                    SubscriptionRepository.create(Subscription(subscriptionDTO, userId, new DateTime, None, None))
                    Ok(HttpResponseUtil.success("Successfully created subscription!"))
            },
            invalid = {
                errors => BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
            }
      )
  }
  
  def update(subscriptionId: Long) = IsAuthenticated(parse.json){ username => implicit request =>
      //logger.info("in SubscriptionController.update(${subscriptionId})")
      println("in SubscriptionController.update(${subscriptionId})")
      val json = request.body.asInstanceOf[JsObject]
      json.validate[SubscriptionDTO].fold(
            valid = { subscriptionDTO =>
                    val optSubscription = SubscriptionRepository.find(subscriptionDTO.subscriptionId.get)
                    optSubscription match {
                      case Some(subscription) => 
                            val updatedSubscription = Subscription(subscriptionDTO, subscription.createdUserId, 
                                                              subscription.createdAt, Some(userId), Some(new DateTime))
                            SubscriptionRepository.update(updatedSubscription)
                            Ok(HttpResponseUtil.success("Successfully updated subscription!"))
                      case None =>
                            BadRequest(HttpResponseUtil.error("Unable to find subscription!"))
                    } 
            },
            invalid = {
                errors => BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
            }
      )
  }
  
  def delete(subscriptionId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in SubscriptionController.delete(${subscriptionId})")
      println("in SubscriptionController.delete(${subscriptionId})")
      SubscriptionRepository.delete(subscriptionId);
      Ok(HttpResponseUtil.success("Successfully deleted subscription!"))
  }
  
  def get(subscriptionId: Long) = IsAuthenticated{ username => implicit request =>
      //logger.info("in SubscriptionController.get(${subscriptionId})")
      println("in SubscriptionController.get(${subscriptionId})")
      var optSubscription = SubscriptionRepository.get(subscriptionId)
      val data = Json.toJson(optSubscription)
      Ok(data).as(JSON)
  }
  
  def getAll = IsAuthenticated{ username => implicit request =>
      //logger.info("in SubscriptionController.getAll...")
      println("in SubscriptionController.getAll")
      var list = SubscriptionRepository.getAll
      val data = Json.toJson(list)
      Ok(data).as(JSON)
  }
}