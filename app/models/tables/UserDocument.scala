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
 * Intersection table between user and document tables
 * 
 */

class UserDocuments(tag: Tag) extends Table[UserDocument](tag, "user_document") {

  def userId = column[Long]("user_id", O.NotNull)
  
  def documentId = column[Long]("document_id", O.NotNull)
  
  def ownershipType = column[OwnershipType]("ownership_type", O.NotNull)
  
  def canCopy = column[Boolean]("can_copy", O.NotNull, O.Default(false))
  
  def canShare = column[Boolean]("can_share", O.NotNull, O.Default(true))
  
  def canView = column[Boolean]("can_view", O.NotNull, O.Default(true))
  
  def isLimitedShare = column[Boolean]("is_limited_share", O.NotNull, O.Default(true))
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def shareUntilEOD = column[DateTime]("share_until_eod", O.Nullable)
  
  def updatedUserId = column[Long]("updated_user_id", O.Nullable)
  
  def updatedAt = column[DateTime]("updated_at", O.Nullable)
  
  override def * = (userId, documentId, ownershipType, canCopy, canShare, canView, isLimitedShare, createdUserId, createdAt, shareUntilEOD.?, updatedUserId.?, updatedAt.?) <> (UserDocument.tupled, UserDocument.unapply)
  
  def ? = (userId.?, documentId.?, ownershipType.?, canCopy.?, canShare.?, canView.?, isLimitedShare.?, createdUserId.?, createdAt.?, shareUntilEOD.?, updatedUserId.?, updatedAt.?)

//  def optionUnapply(oc: Option[Supplier]): Option[(Option[Int], Option[String], Option[String])] = None)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_user_document", (userId, documentId))
  
  def owner = foreignKey("fk_user_document_on_user_id", userId, TableQuery[Users])(_.userId)
  
  def document = foreignKey("fk_user_document_on_document_id", documentId, TableQuery[Documents])(_.documentId)
  
  def createdBy = foreignKey("fk_on_user_document_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_user_document_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
