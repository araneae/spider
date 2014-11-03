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
 * User can attach multiple tags to each document.
 * 
 */

class DocumentTags(tag: Tag) extends Table[DocumentTag](tag, "document_tag") {

  def userId = column[Long]("user_id", O.NotNull)
  
  def userTagId = column[Long]("user_tag_id", O.NotNull)
  
  def documentId = column[Long]("document_id", O.NotNull)
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Option[Long]]("updated_user_id", O.Nullable)
  
  def updatedAt = column[Option[DateTime]]("updated_at", O.Nullable)
  
  override def * = (userId, userTagId, documentId, createdUserId, createdAt, updatedUserId, updatedAt) <> (DocumentTag.tupled, DocumentTag.unapply)
  
  def user = foreignKey("fk_on_document_tag_user_id", userId, TableQuery[Users])(_.userId)
  
  def userTag = foreignKey("fk_on_document_tag_tag_id", userTagId, TableQuery[UserTags])(_.userTagId)
  
  def document = foreignKey("fk_on_document_tag_document_id", documentId, TableQuery[Documents])(_.documentId)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_document_tag_userid_user_tag_id_document_id", (userId, userTagId, documentId))
  
  def createdBy = foreignKey("fk_on_document_tag_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_document_tag_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
