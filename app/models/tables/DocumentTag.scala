package models.tables

import play.api.db.slick.Config.driver.simple._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import models.dtos._

/**
 * User can attach multiple tags to each document.
 * 
 */

class DocumentTags(tag: Tag) extends Table[DocumentTag](tag, "document_tag") {

  def userId = column[Long]("user_id", O.NotNull)
  
  def userTagId = column[Long]("user_tag_id", O.NotNull)
  
  def documentId = column[Long]("document_id", O.NotNull)
  
  override def * = (userId, userTagId, documentId) <> (DocumentTag.tupled, DocumentTag.unapply)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_document_tag_userid_user_tag_id_document_id", (userId, userTagId, documentId))

}
