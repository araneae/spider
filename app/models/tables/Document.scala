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
 * User can upload multiple resumes.
 * 
 */

class Documents(tag: Tag) extends Table[Document](tag, "document") {

  def documentId = column[Long]("document_id", O.PrimaryKey, O.AutoInc)
  
  def documentFolderId = column[Long]("document_folder_id")
  
  def name = column[String]("name")
  
  def documentType = column[DocumentType]("document_type")
  
  def fileType = column[FileType]("file_type")
  
  def fileName = column[String]("file_name")
  
  def physicalName = column[String]("physical_name")
  
  def description = column[String]("description")
  
  def signature = column[String]("signature")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (documentId.?, documentFolderId, name, documentType, fileType, fileName, physicalName, description, signature, createdUserId, createdAt, updatedUserId, updatedAt) <> (Document.tupled, Document.unapply)
  
  // foreign keys and indexes
  def documentBox = foreignKey("fk_document_on_document_folder_id", documentFolderId.?, TableQuery[DocumentFolders])(_.documentFolderId)
  
  def createdBy = foreignKey("fk_document_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_document_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
