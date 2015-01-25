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
  
  def userId = column[Long]("user_id")
  
  def messageBoxType = column[MessageBoxType]("message_box_type")
  
  def name = column[String]("name")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (messageBoxId.?, userId, messageBoxType, name, createdUserId, createdAt, updatedUserId, updatedAt) <> (MessageBox.tupled, MessageBox.unapply _)
  
  // foreign keys and indexes
  def owner = foreignKey("fk_message_box_on_user_id", userId, TableQuery[Users])(_.userId)
  
  def createdBy = foreignKey("fk_message_box_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_message_box_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
