package traits

import controllers.routes
import play.api.db.slick.DBAction
import play.api.db.slick.DBSessionRequest
import be.objectify.deadbolt.scala.DeadboltActions
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.BodyParser
import play.api.mvc.Request
import play.api.mvc.RequestHeader
import play.api.mvc.Results
import play.api.mvc.Security
import play.api.mvc.Result
import play.api.mvc.Session
import play.api.mvc.Flash
import java.util.{Date, Locale}
import scala.concurrent.Future
import utils._
import security._

trait Secured extends DeadboltActions with GlobalConstnts {
  
  //def username(request: RequestHeader) = request.session.get(Security.username)
  def username(request: RequestHeader) : Option[String] = {
     val optValue = request.session.get(Security.username)
     optValue
//  commenting out the session timeout - might be useful later on some different ways
//     optValue match {
//       case Some(value) =>
//               // see if the session is expired
//               val optUserTime = request.session.get(PARAM_USER_TIME);
//               optUserTime match {
//                   case Some(userTime) =>
//                       val userTicks = userTime.toLong 
//                       val currentTicks = new Date().getTime()
//                       val sessionTimeOut = Configuration.sessionTimeoutInMillis
//                       val diff = currentTicks - userTicks
//                       if (diff > sessionTimeOut) {
//                          return None
//                       }
//                   case None =>
//               }
//               optValue
//       case None => None
//     }
  }
  
  def userId(implicit request : RequestHeader) = {
      val id : String = request.session.get(PARAM_USER_ID).getOrElse(INVALID_USER_ID);
      id.toLong
  }
  
  def name(implicit request: RequestHeader) = {
      request.session.get(PARAM_NAME).getOrElse(EMPTY)
  }
  
  def path(implicit request: RequestHeader) = {
      request.session.get(PARAM_PATH).getOrElse(EMPTY)
  }
  
  def onUnauthorized(request: RequestHeader) = {
    // check if the session has userId
    //val id = request.session.get(PARAM_USER_ID)
    //id match {
    //  case Some(value) =>
    //    // user has timeout, send unauthorized response
    //    Results.Unauthorized
    //  case None =>
    //    // user hasn't logged in, redirect to login page
    //    Results.Redirect(routes.AuthController.login).withSession(PARAM_PATH -> request.path)
    //} 
    Results.Unauthorized
  }

  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthorized) { 
        user => Action.async(request => {
                            // update time in session
                            val currentTicks = new Date().getTime()
                            val result = f(user)(request).withSession(request.session + (PARAM_USER_TIME -> currentTicks.toString))
                            Future.successful(result)
                          }
                       )
     }
  }
  
  def IsAuthenticated(b: BodyParser[Any]) (f: => String => Request[Any] => Result) = {
    Security.Authenticated(username, onUnauthorized) { 
      user => Action.async(b)(request => {
                            // update time in session
                            val currentTicks = new Date().getTime()
                            val result = f(user)(request).withSession(request.session + (PARAM_USER_TIME -> currentTicks.toString))
                            Future.successful(result)
                          }
                        )
    }
  }
  
  def IsAuthorized(role: String) (f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthorized) { 
        user =>   Restrict(List(Array(role), Array(ROLE_SITE_ADMIN)), new DefaultDeadboltHandler()) {
                          Action.async(request => {
                            // update time in session
                            val currentTicks = new Date().getTime()
                            val result = f(user)(request).withSession(request.session + (PARAM_USER_TIME -> currentTicks.toString))
                            Future.successful(result)
                          }
                      )
                }
     }
  }
  
  def IsAuthorized(b: BodyParser[Any])(role: String)(f: => String => Request[Any] => Result) = {
    Security.Authenticated(username, onUnauthorized) {
      user =>  Restrict(List(Array(role), Array(ROLE_SITE_ADMIN)), new DefaultDeadboltHandler()) {
                        Action.async(b)(request => {
                            // update time in session
                            val currentTicks = new Date().getTime()
                            val result = f(user)(request).withSession(request.session + (PARAM_USER_TIME -> currentTicks.toString))
                            Future.successful(result)
                          }
                        )
            }
    }
  }
  
//  def IsAuthenticatedWithDBAction(f: => String => DBSessionRequest[_] => SimpleResult) =
//     Security.Authenticated(username, onUnauthorized) {
//         user => DBAction(rs => f(user)(rs))
//  }
  
//  def IsAuthenticatedWithDBAction(b: BodyParser[Any])(f: => String => DBSessionRequest[_] => SimpleResult) =
//     Security.Authenticated(username, onUnauthorized) {
//         user => DBAction(b)(request => f(user)(request))
//  }
}