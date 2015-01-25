package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

/**
 * For storing user messages
 *  
 */

class UserMessages(tag: Tag) extends Table[UserMessage](tag, "user_message") {

  def userId = column[Long]("user_id")
  
  def messageId = column[Long]("message_id")
  
  def messageBoxId = column[Long]("message_box_id")
  
  def read = column[Boolean]("read", O.Default(false))
  
  def replied = column[Boolean]("replied", O.Default(false))
  
  def important = column[Boolean]("important", O.Default(false))
  
  def star = column[Boolean]("star", O.Default(false))
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (userId, messageId, messageBoxId, read, replied, important, star, createdUserId, createdAt, updatedUserId, updatedAt) <> (UserMessage.tupled, UserMessage.unapply)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_user_message", (userId, messageId, messageBoxId))
  
  def owner = foreignKey("fk_user_message_on_user_id",  userId, TableQuery[Users])(_.userId)
  
  def message = foreignKey("fk_user_message_on_message_id", messageId, TableQuery[Messages])(_.messageId)
  
  def messageBox = foreignKey("fk_user_message_on_message_box_id", messageBoxId, TableQuery[MessageBoxes])(_.messageBoxId)
  
  def createdBy = foreignKey("fk_user_message_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_user_message_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
