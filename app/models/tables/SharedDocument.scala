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
 * User can have shared documents.
 * 
 */

class SharedDocuments(tag: Tag) extends Table[SharedDocument](tag, "shared_document") {

  def userId = column[Long]("user_id", O.NotNull)
  
  def documentId = column[Long]("document_id", O.NotNull)
  
  def sharedByUserId = column[Long]("shared_by_user_id", O.NotNull)
  
  def canCopy = column[Boolean]("can_copy", O.NotNull, O.Default(false))
  
  def canShare = column[Boolean]("can_share", O.NotNull, O.Default(true))
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Long]("updated_user_id", O.Nullable)
  
  def updatedAt = column[DateTime]("updated_at", O.Nullable)
  
  override def * = (userId, documentId, sharedByUserId, canCopy, canShare, createdUserId, createdAt, updatedUserId.?, updatedAt.?) <> (SharedDocument.tupled, SharedDocument.unapply)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_shared_document", (userId, documentId))
  
  def owner = foreignKey("fk_shared_document_on_user_id", userId, TableQuery[Users])(_.userId)
  
  def sharedBy = foreignKey("fk_shared_document_on_shared_user_id", sharedByUserId, TableQuery[Users])(_.userId)
  
  def document = foreignKey("fk_shared_document_on_document_id", documentId, TableQuery[Documents])(_.documentId)
  
  def createdBy = foreignKey("fk_on_shared_document_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_shared_document_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
