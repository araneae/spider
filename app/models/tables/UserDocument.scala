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

  def userDocumentId = column[Long]("user_document_id", O.PrimaryKey, O.AutoInc)
  
  def userId = column[Long]("user_id")
  
  def documentId = column[Long]("document_id")
  
  def ownershipType = column[OwnershipType]("ownership_type")
  
  def canCopy = column[Boolean]("can_copy", O.Default(false))
  
  def canShare = column[Boolean]("can_share", O.Default(true))
  
  def canView = column[Boolean]("can_view", O.Default(true))
  
  def important = column[Boolean]("important", O.Default(false))
  
  def star = column[Boolean]("star", O.Default(false))
  
  def isLimitedShare = column[Boolean]("is_limited_share", O.Default(true))
  
  def shareUntilEOD = column[Option[DateTime]]("share_until_eod")

  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (userDocumentId.?, userId, documentId, ownershipType, canCopy, canShare, canView, important, star, isLimitedShare, shareUntilEOD, createdUserId, createdAt, updatedUserId, updatedAt) <> (UserDocument.tupled, UserDocument.unapply)
  
  def ? = (userDocumentId.?, userId.?, documentId.?, ownershipType.?, canCopy.?, canShare.?, canView.?, important.?, star.?, isLimitedShare.?, shareUntilEOD, createdUserId.?, createdAt.?, updatedUserId, updatedAt)

//  def optionUnapply(oc: Option[Supplier]): Option[(Option[Int], Option[String], Option[String])] = None)
  
  // foreign keys and indexes
  def owner = foreignKey("fk_user_document_on_user_id", userId, TableQuery[Users])(_.userId)
  
  def document = foreignKey("fk_user_document_on_document_id", documentId, TableQuery[Documents])(_.documentId)
  
  def createdBy = foreignKey("fk_user_document_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_user_document_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
  
  def uniqueDocument = index("idx_user_document_on_user_id_document_id", (userId, documentId), unique = true)
}
