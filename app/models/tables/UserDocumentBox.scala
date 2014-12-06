package models.tables

import play.api.db.slick.Config.driver.simple._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime
import enums.OwnershipType._

/**
 * Intersection table between user and document box
 * 
 */

class UserDocumentBoxes(tag: Tag) extends Table[UserDocumentBox](tag, "user_document_box") {

  def userDocumentBoxId = column[Long]("user_document_box_id", O.PrimaryKey, O.AutoInc)
  
  def documentBoxId = column[Long]("document_box_id", O.NotNull)
  
  def userId = column[Long]("user_id", O.NotNull)
  
  def ownershipType = column[OwnershipType]("ownership_type", O.NotNull)
  
  def canCopy = column[Boolean]("can_copy", O.NotNull, O.Default(true))
  
  def isLimitedShare = column[Boolean]("is_limited_share", O.NotNull, O.Default(true))
  
  def shareUntilEOD = column[Option[DateTime]]("share_until_eod", O.Nullable)
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Option[Long]]("updated_user_id", O.Nullable)
  
  def updatedAt = column[Option[DateTime]]("updated_at", O.Nullable)
  
  override def * = (userDocumentBoxId.?, documentBoxId, userId, ownershipType, canCopy, isLimitedShare, shareUntilEOD, createdUserId, createdAt, updatedUserId, updatedAt) <> (UserDocumentBox.tupled, UserDocumentBox.unapply)
  
  def ? = (userDocumentBoxId.?, documentBoxId.?, userId.?, ownershipType.?, canCopy.?, isLimitedShare.?, shareUntilEOD, createdUserId.?, createdAt.?, updatedUserId, updatedAt)
  
  // foreign keys and indexes
  def documentBox = foreignKey("fk_user_document_box_on_document_box_id", documentBoxId, TableQuery[DocumentBoxes])(_.documentBoxId)
  
  def owner = foreignKey("fk_user_document_box_on_user_id", userId, TableQuery[Users])(_.userId)
  
  def createdBy = foreignKey("fk_on_user_document_box_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_user_document_box_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
