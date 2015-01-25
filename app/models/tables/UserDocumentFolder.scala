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
 * Intersection table between user and document folder
 * 
 */

class UserDocumentFolders(tag: Tag) extends Table[UserDocumentFolder](tag, "user_document_folder") {

  def userDocumentFolderId = column[Long]("user_document_folder_id", O.PrimaryKey, O.AutoInc)
  
  def documentFolderId = column[Long]("document_folder_id")
  
  def userId = column[Long]("user_id")
  
  def ownershipType = column[OwnershipType]("ownership_type")
  
  def canCopy = column[Boolean]("can_copy", O.Default(true))
  
  def canShare = column[Boolean]("can_share", O.Default(true))
  
  def canView = column[Boolean]("can_view", O.Default(true))
  
  def isLimitedShare = column[Boolean]("is_limited_share", O.Default(false))
  
  def shareUntilEOD = column[Option[DateTime]]("share_until_eod")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (userDocumentFolderId.?, documentFolderId, userId, ownershipType, canCopy, canShare, canView, isLimitedShare, shareUntilEOD, createdUserId, createdAt, updatedUserId, updatedAt) <> (UserDocumentFolder.tupled, UserDocumentFolder.unapply)
  
  def ? = (userDocumentFolderId.?, documentFolderId.?, userId.?, ownershipType.?, canCopy.?, canShare.?, canView.?, isLimitedShare.?, shareUntilEOD, createdUserId.?, createdAt.?, updatedUserId, updatedAt)
  
  // foreign keys and indexes
  def documentFolder = foreignKey("fk_user_document_folder_on_document_folder_id", documentFolderId, TableQuery[DocumentFolders])(_.documentFolderId)
  
  def owner = foreignKey("fk_user_document_folder_on_user_id", userId, TableQuery[Users])(_.userId)
  
  def createdBy = foreignKey("fk_user_document_folder_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_user_document_folder_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
  
  def uniqueDocumentFolder = index("idx_user_document_folder_on_user_id_document_id", (userId, documentFolderId), unique = true)
}
