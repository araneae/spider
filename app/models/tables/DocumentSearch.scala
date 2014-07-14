package models.tables

import play.api.db.slick.Config.driver.simple._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import models.dtos._

/**
 * User's saved search texts.
 * 
 */

class DocumentSearches(tag: Tag) extends Table[DocumentSearch](tag, "document_search") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  
  def userId = column[Long]("user_id", O.NotNull)
  
  def name = column[String]("name", O.NotNull)
  
  def searchText = column[String]("search_text", O.NotNull)
  
  override def * = (id.?, userId, name, searchText) <> (DocumentSearch.tupled, DocumentSearch.unapply)
  
  def user = foreignKey("fk_on_document_search_user_id", userId, TableQuery[Users])(_.id)
  
  // foreign keys and indexes

}
