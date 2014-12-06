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
  
  def documentBoxId = column[Long]("document_box_id", O.NotNull)
  
  def name = column[String]("name", O.NotNull)
  
  def documentType = column[DocumentType]("document_type", O.NotNull)
  
  def fileType = column[FileType]("file_type", O.NotNull)
  
  def fileName = column[String]("file_name", O.NotNull)
  
  def physicalName = column[String]("physical_name", O.NotNull)
  
  def description = column[Option[String]]("description", O.Nullable)
  
  def signature = column[String]("signature", O.NotNull)
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Option[Long]]("updated_user_id", O.Nullable)
  
  def updatedAt = column[Option[DateTime]]("updated_at", O.Nullable)
  
  override def * = (documentId.?, documentBoxId, name, documentType, fileType, fileName, physicalName, description, signature, createdUserId, createdAt, updatedUserId, updatedAt) <> (Document.tupled, Document.unapply)
  
  // foreign keys and indexes
  def documentBox = foreignKey("fk_document_on_document_box_id", documentBoxId, TableQuery[DocumentBoxes])(_.documentBoxId)
  
  def createdBy = foreignKey("fk_on_document_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_document_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
