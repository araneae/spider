package models.tables

import play.api.db.slick.Config.driver.simple._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime
import enums.SearchQueryType._

/**
 * User's saved search quries.
 * 
 */

class SearchQueries(tag: Tag) extends Table[SearchQuery](tag, "search_query") {

  def searchQueryId = column[Long]("search_query_id", O.PrimaryKey, O.AutoInc)
  
  def userId = column[Long]("user_id")
  
  def queryType = column[SearchQueryType]("query_type")
  
  def name = column[String]("name")
  
  def query = column[String]("query")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (searchQueryId.?, userId, queryType, name, query, createdUserId, createdAt, updatedUserId, updatedAt) <> (SearchQuery.tupled, SearchQuery.unapply)
  
  // foreign keys and indexes
  def user = foreignKey("fk_search_query_on_user_id", userId, TableQuery[Users])(_.userId)
  
  def createdBy = foreignKey("fk_search_query_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_search_query_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
