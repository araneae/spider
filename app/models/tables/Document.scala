package models.tables

import play.api.db.slick.Config.driver.simple._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import models.dtos._

/**
 * User can upload multiple resumes.
 * 
 */

class Documents(tag: Tag) extends Table[Document](tag, "document") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  
  def userId = column[Long]("user_id", O.NotNull)
  
  def name = column[String]("name", O.NotNull)
  
  def documentType = column[DocumentType]("document_type", O.NotNull)
  
  def fileType = column[FileType]("file_type", O.NotNull)
  
  def fileName = column[String]("file_name", O.NotNull)
  
  def physicalName = column[String]("physical_name", O.NotNull)
  
  def description = column[String]("description", O.Nullable)
  
  override def * = (id.?, userId, name, documentType, fileType, fileName, physicalName, description) <> (Document.tupled, Document.unapply)
  
  // foreign keys and indexes
  def owner = foreignKey("fk_document_on_user_id", userId, TableQuery[Users])(_.id)
  
  //def uniqueCode = index("idx_document_on_user_doc_filename_unique", (userId, documentType, fileName), unique = true)
}
