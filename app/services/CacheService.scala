package services

import play.mvc._
import com.typesafe.plugin._
import play.api.Play.current
import models.tables._
import models.dtos._
import utils.Configuration
import play.api.mvc.Request
import play.api.mvc.Security
import java.util.concurrent.Callable
import play.cache.Cache
import security._
import models.repositories.UserRepository
import traits._

object CacheService extends GlobalConstnts {
 
   def getDeadboltUser(request: Request[Any]): Option[DeadboltUser] = {
     println("in CacheService.getDeadboltUser(...)")
     val optUsername = getUsername(request)
     optUsername match {
       case Some(username) =>
                 val cacheKey = getCacheKey(USER_DATA_CACHE_TYPE, DEADBOLT_USER_CACHE_KEY, username)
                 val user = Cache.getOrElse[DeadboltUser](cacheKey, 
                                           new Callable[DeadboltUser] {
                                              def call() = {
                                                 val userRoles = if (Configuration.isSiteAdmin(username)) List(ROLE_SITE_ADMIN) else List()
                                                 val deadboltUser = new DeadboltUser(username, userRoles)
                                                 deadboltUser
                                              }
                                        }, USER_DATA_CACHE_TIMEOUT_IN_SECS)
                 Some(user)
       case None => None
     }
   }
   
   def getCacheKey(keyType: String, keyValue: String, username: String) = s"${keyType}.${keyValue}.${username}"

   def getUsername(request: Request[Any]): Option[String] = {
     val optValue = request.session.get(Security.username)
     optValue
   }
}