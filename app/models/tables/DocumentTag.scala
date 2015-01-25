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

  def userId = column[Long]("user_id")
  
  def userTagId = column[Long]("user_tag_id")
  
  def documentId = column[Long]("document_id")
  
  def userDocumentId = column[Long]("user_document_id")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (userId, userTagId, documentId, userDocumentId, createdUserId, createdAt, updatedUserId, updatedAt) <> (DocumentTag.tupled, DocumentTag.unapply)
  
  // foreign keys and indexes
  def user = foreignKey("fk_document_tag_on_user_id", userId, TableQuery[Users])(_.userId)
  
  def userTag = foreignKey("fk_document_tag_on_tag_id", userTagId, TableQuery[UserTags])(_.userTagId)
  
  def document = foreignKey("fk_document_tag_on_document_id", documentId, TableQuery[Documents])(_.documentId)
  
  def userDocument = foreignKey("fk_document_tag_on_user_document_id", userDocumentId, TableQuery[UserDocuments])(_.userDocumentId)
  
  def createdBy = foreignKey("fk_document_tag_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_document_tag_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
  
  def uniqueUserTagDocument = index("idx_document_tag_on_user_tag_document", (userTagId, documentId), unique = true)
}
