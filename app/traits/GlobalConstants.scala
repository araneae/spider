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

trait GlobalConstnts {
  
  final val PARAM_USER_TIME = "userTime"
  final val PARAM_USER_ID = "userId"
  final val PARAM_NAME = "name"
  final val PARAM_PATH = "path"
  final val EMPTY = ""
  final val INVALID_USER_ID = "0"
  final val ROLE_SITE_ADMIN = "site.admin"
  
  // cache management
  final val USER_DATA_CACHE_TYPE = "user"
  final val DEADBOLT_USER_CACHE_KEY = "deadbolt"
  final val USER_DATA_CACHE_TIMEOUT_IN_SECS = 20 * 60 
 
}