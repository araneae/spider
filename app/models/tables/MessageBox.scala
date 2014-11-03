package models.tables

import play.api.db.slick.Config.driver.simple._
import enums.MessageBoxType._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

/**
 * Message box
 * 
 */

class MessageBoxes(tag: Tag) extends Table[MessageBox](tag, "message_box") {

  def messageBoxId = column[Long]("message_box_id", O.PrimaryKey, O.AutoInc)
  
  def userId = column[Long]("user_id", O.NotNull)
  
  def messageBoxType = column[MessageBoxType]("message_box_type", O.NotNull)
  
  def name = column[String]("name", O.NotNull)
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Option[Long]]("updated_user_id", O.Nullable)
  
  def updatedAt = column[Option[DateTime]]("updated_at", O.Nullable)
  
  override def * = (messageBoxId.?, userId, messageBoxType, name, createdUserId, createdAt, updatedUserId, updatedAt) <> (MessageBox.tupled, MessageBox.unapply _)
  
  // foreign keys and indexes
  def owner = foreignKey("fk_on_message_box_user_id", userId, TableQuery[Users])(_.userId)
  
  def createdBy = foreignKey("fk_on_message_box_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_message_box_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
