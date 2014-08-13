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

  def userId = column[Long]("user_id", O.NotNull)
  
  def messageId = column[Long]("message_id", O.NotNull)
  
  def messageBoxId = column[Long]("message_box_id", O.NotNull)
  
  def read = column[Boolean]("read", O.NotNull, O.Default(false))
  
  def important = column[Boolean]("important", O.NotNull, O.Default(false))
  
  def star = column[Boolean]("star", O.NotNull, O.Default(false))
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Long]("updated_user_id", O.Nullable)
  
  def updatedAt = column[DateTime]("updated_at", O.Nullable)
  
  override def * = (userId, messageId, messageBoxId, read, important, star, createdUserId, createdAt, updatedUserId.?, updatedAt.?) <> (UserMessage.tupled, UserMessage.unapply)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_user_message", (userId, messageId, messageBoxId))
  
  def owner = foreignKey("fk_on_user_message_user_id",  userId, TableQuery[Users])(_.userId)
  
  def message = foreignKey("fk_on_user_message_message_id", messageId, TableQuery[Messages])(_.messageId)
  
  def messageBox = foreignKey("fk_on_user_message_message_box_id", messageBoxId, TableQuery[MessageBoxes])(_.messageBoxId)
  
  def createdBy = foreignKey("fk_on_user_message_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_user_message_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
