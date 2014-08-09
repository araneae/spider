package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._

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
  
  override def * = (userId, messageId, messageBoxId, read, important, star) <> (UserMessage.tupled, UserMessage.unapply)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_user_message", (userId, messageId, messageBoxId))
  
  def owner = foreignKey("fk_on_user_message_user_id",  userId, TableQuery[Users])(_.userId)
  
  def message = foreignKey("fk_on_user_message_message_id", messageId, TableQuery[Messages])(_.messageId)
  
  def messageBox = foreignKey("fk_on_user_message_message_box_id", messageBoxId, TableQuery[MessageBoxes])(_.messageBoxId)
}
