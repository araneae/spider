package models.tables

import play.api.db.slick.Config.driver.simple._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import models.dtos._

/**
 * User can have shared documents.
 * 
 */

class SharedDocuments(tag: Tag) extends Table[SharedDocument](tag, "shared_document") {

  def userId = column[Long]("user_id", O.NotNull)
  
  def documentId = column[Long]("document_id", O.NotNull)
  
  def sharedUserId = column[Long]("shared_user_id", O.NotNull)
  
  def canCopy = column[Boolean]("can_copy", O.NotNull, O.Default(false))
  
  override def * = (userId, documentId, sharedUserId, canCopy) <> (SharedDocument.tupled, SharedDocument.unapply)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_shared_document", (userId, documentId))
  
  def owner = foreignKey("fk_shared_document_on_user_id", userId, TableQuery[Users])(_.id)
  
  def sharedBy = foreignKey("fk_shared_document_on_shared_user_id", sharedUserId, TableQuery[Users])(_.id)
  
  def document = foreignKey("fk_shared_document_on_document_id", documentId, TableQuery[Documents])(_.id)
}
