package utils

import play.api.Play

object Configuration {
  
  private val baseUrl = Play.current.configuration.getString("application.baseUrl").getOrElse("http://localhost:9000")
  private val basePath = Play.current.configuration.getString("upload.base.path").getOrElse("/tmp")
  private val lucenePath = Play.current.configuration.getString("lucene.index.path").getOrElse("/tmp/index")
  
  def applicationBaseUrl = baseUrl
  
  def uploadBasePath = basePath
  
  def uploadFilePath(userId: Long, fileName: String) = s"${basePath}/${userId}/${fileName}"
  
  def uploadPath(userId: Long) = s"${basePath}/${userId}"
  
  def luceneIndexPath = lucenePath
}