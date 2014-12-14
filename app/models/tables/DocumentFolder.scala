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
 * Document folder.
 * 
 */

class DocumentFolders(tag: Tag) extends Table[DocumentFolder](tag, "document_folder") {

  def documentFolderId = column[Long]("document_folder_id", O.PrimaryKey, O.AutoInc)
  
  def name = column[String]("name")
  
  def default = column[Boolean]("default")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (documentFolderId.?, name, default, createdUserId, createdAt, updatedUserId, updatedAt) <> (DocumentFolder.tupled, DocumentFolder.unapply)
  
  // foreign keys and indexes
  
  def createdBy = foreignKey("fk_on_document_folder_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_document_folder_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
