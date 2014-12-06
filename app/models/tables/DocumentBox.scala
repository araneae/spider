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
 * Document box.
 * 
 */

class DocumentBoxes(tag: Tag) extends Table[DocumentBox](tag, "document_box") {

  def documentBoxId = column[Long]("document_box_id", O.PrimaryKey, O.AutoInc)
  
  def name = column[String]("name", O.NotNull)
  
  def description = column[Option[String]]("description", O.Nullable)
  
  def default = column[Boolean]("default", O.NotNull)
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Option[Long]]("updated_user_id", O.Nullable)
  
  def updatedAt = column[Option[DateTime]]("updated_at", O.Nullable)
  
  override def * = (documentBoxId.?, name, description, default, createdUserId, createdAt, updatedUserId, updatedAt) <> (DocumentBox.tupled, DocumentBox.unapply)
  
  // foreign keys and indexes
  
  def createdBy = foreignKey("fk_on_document_box_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_document_box_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
