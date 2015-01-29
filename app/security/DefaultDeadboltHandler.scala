package security

import be.objectify.deadbolt.scala.{DynamicResourceHandler, DeadboltHandler}
import play.api.mvc.{Request, Result, Results}
import be.objectify.deadbolt.core.models.Subject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import controllers.routes
import utils.HttpResponseUtil
import services._

class DefaultDeadboltHandler(dynamicResourceHandler: Option[DynamicResourceHandler] = None) extends DeadboltHandler {

  def beforeAuthCheck[A](request: Request[A]) = None

  override def getSubject[A](request: Request[A]): Future[Option[Subject]] = {
    val user = CacheService.getDeadboltUser(request)
    Future(user)
  }

  def onAuthFailure[A](request: Request[A]): Future[Result] = {
    Future {Results.Forbidden(HttpResponseUtil.error("Forbidden access!"))}
  }
  
  def getDynamicResourceHandler[A](request: Request[A]): Option[DynamicResourceHandler] = dynamicResourceHandler
  
}