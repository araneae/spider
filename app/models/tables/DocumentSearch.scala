package models.tables

import play.api.db.slick.Config.driver.simple._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

/**
 * User's saved search texts.
 * 
 */

class DocumentSearches(tag: Tag) extends Table[DocumentSearch](tag, "document_search") {

  def documentSearchId = column[Long]("document_search_id", O.PrimaryKey, O.AutoInc)
  
  def userId = column[Long]("user_id")
  
  def name = column[String]("name")
  
  def searchText = column[String]("search_text")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (documentSearchId.?, userId, name, searchText, createdUserId, createdAt, updatedUserId, updatedAt) <> (DocumentSearch.tupled, DocumentSearch.unapply)
  
  // foreign keys and indexes
  def user = foreignKey("fk_on_document_search_user_id", userId, TableQuery[Users])(_.userId)
  
  def createdBy = foreignKey("fk_on_document_search_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_document_search_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
