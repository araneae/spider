package traits

import controllers.routes
import play.api.db.slick.DBAction
import play.api.db.slick.DBSessionRequest
import play.api.mvc.Action
import play.api.mvc.AnyContent
import play.api.mvc.BodyParser
import play.api.mvc.Request
import play.api.mvc.RequestHeader
import play.api.mvc.Result
import play.api.mvc.Results
import play.api.mvc.Security
import play.api.mvc.SimpleResult
import play.api.mvc.Session
import play.api.mvc.Flash

trait Secured {

  def username(request: RequestHeader) = request.session.get(Security.username)
  
  def userId(implicit request : RequestHeader) = {
      val id : String = request.session.get("userId").getOrElse("0");
      id.toInt
  }
  
  def name(implicit request : RequestHeader) = {
      request.session.get("name").getOrElse("")
  }
  
  def path(implicit request : RequestHeader) = {
      request.session.get("path").getOrElse("")
  }

  def onUnauthorized(request: RequestHeader) = {
    Results.Redirect(routes.AuthController.login).withSession("path" -> request.path)
  }

  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = {
    Security.Authenticated(username, onUnauthorized) { 
        user => Action(request => f(user)(request))
     }
  }
  
  def IsAuthenticated(b: BodyParser[Any]) (f: => String => Request[Any] => Result) = {
    Security.Authenticated(username, onUnauthorized) { 
      user => Action(b)(request => f(user)(request))
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