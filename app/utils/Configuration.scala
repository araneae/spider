package utils

import play.api.Play
import collection.JavaConversions._

object Configuration {
  
  private val appTitle = Play.current.configuration.getString("application.title").getOrElse("Araneae")
  private val appName = Play.current.configuration.getString("application.name").getOrElse("Araneae")
  private val baseUrl = Play.current.configuration.getString("application.baseUrl").getOrElse("http://localhost:9000")
  private val basePath = Play.current.configuration.getString("upload.base.path").getOrElse("/tmp")
  private val imagePath = Play.current.configuration.getString("image.base.path").getOrElse("/tmp")
  private val lucenePath = Play.current.configuration.getString("lucene.index.path").getOrElse("/tmp/lucene")
  private val timeoutInMins : Long = Play.current.configuration.getLong("sessionTimeoutInMins").getOrElse(10)
  private val otpTimeoutInMins : Int = Play.current.configuration.getInt("otp.password.timeout.mins").getOrElse(10)
  private val xrayTerms: String = Play.current.configuration.getString("default.xray.terms").getOrElse("")
  private val defaultPictureUrl = Play.current.configuration.getString("default.profile.picture.url").getOrElse("")
  private val profilePictureUrl = Play.current.configuration.getString("profile.picture.url").getOrElse("")
  private val siteAdminUserIds = {
                   val optList = Play.current.configuration.getStringList("site.admin.usernames")
                   optList match {
                     case Some(list) => list.toList
                     case None => List[String]()
                   }
              }
  private val timeoutInMillis = timeoutInMins * 1000 * 60
  
  def applicationTitle = appTitle
  
  def applicationName = appName
  
  def applicationBaseUrl = baseUrl
  
  def uploadBasePath = basePath
  
  def uploadFilePath(fileName: String) = s"${basePath}/${fileName}"
  
  def uploadUserTempFilePath(userId: Long, fileName: String) = s"${basePath}/temp/${userId}/${fileName}"
  
  def uploadUserTempPath(userId: Long) = s"${basePath}/temp/${userId}"
  
  def uploadImageBasePath = imagePath
  
  def uploadImageFilePath(fileName: String) = s"${imagePath}/${fileName}"
  
  def luceneIndexPath = lucenePath
  
  def sessionTimeoutInMillis = timeoutInMillis
  
  def otpPasswordTimeoutInMins = otpTimeoutInMins
  
  def defaultXrayTerms = xrayTerms
  
  def defaultProfilePictureUrl = defaultPictureUrl
  
  def userProfilePictureUrl(fileName: String) = s"${profilePictureUrl}/${fileName}"
  
  def isSiteAdmin(username: String) = if (siteAdminUserIds.contains(username)) true else false
}