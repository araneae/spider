package security

import be.objectify.deadbolt.scala.{DynamicResourceHandler, DeadboltHandler}
import play.api.mvc.{Request, Result, Results}
import be.objectify.deadbolt.core.models.Subject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import controllers.routes

class MyDeadboltHandler(dynamicResourceHandler: Option[DynamicResourceHandler] = None) extends DeadboltHandler {

  def beforeAuthCheck[A](request: Request[A]) = None

  override def getSubject[A](request: Request[A]): Future[Option[Subject]] = {
    // e.g. request.session.get("user")
    Future(Some(new DeadboltUser("bubul")))
  }

  def onAuthFailure[A](request: Request[A]): Future[Result] = {
    Future {Results.Redirect(routes.AuthController.login())}
  }
  
  def getDynamicResourceHandler[A](request: Request[A]): Option[DynamicResourceHandler] = None
}