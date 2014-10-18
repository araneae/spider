package utils

import play.api.Play

object Configuration {
  
  private val baseUrl = Play.current.configuration.getString("application.baseUrl").getOrElse("http://localhost:9000")
  private val basePath = Play.current.configuration.getString("upload.base.path").getOrElse("/tmp")
  private val lucenePath = Play.current.configuration.getString("lucene.index.path").getOrElse("/tmp/index")
  private val timeoutInMins : Long = Play.current.configuration.getLong("sessionTimeoutInMins").getOrElse(10)
  private val timeoutInMillis = timeoutInMins * 1000 * 60
  
  def applicationBaseUrl = baseUrl
  
  def uploadBasePath = basePath
  
  def uploadFilePath(fileName: String) = s"${basePath}/${fileName}"
  
  def uploadUserTempFilePath(userId: Long, fileName: String) = s"${basePath}/temp/${userId}/${fileName}"
  
  def uploadUserTempPath(userId: Long) = s"${basePath}/temp/${userId}"
  
  def luceneIndexPath = lucenePath
  
  def sessionTimeoutInMillis = timeoutInMillis
}