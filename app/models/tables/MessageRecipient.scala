package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

/**
 * Message recipients
 *  
 */

class MessageRecipients(tag: Tag) extends Table[MessageRecipient](tag, "message_recipient") {

  def userId = column[Long]("user_id")
  
  def messageId = column[Long]("message_id")
  
  def read = column[Boolean]("read")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (userId, messageId, read, createdUserId, createdAt, updatedUserId, updatedAt) <> (MessageRecipient.tupled, MessageRecipient.unapply _)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_message_recipient", (userId, messageId))
  
  def recipient = foreignKey("fk_message_recipient_on_user_id", userId, TableQuery[Users])(_.userId)
  
  def message = foreignKey("fk_message_recipient_on_message_id", messageId, TableQuery[Messages])(_.messageId)
  
  def createdBy = foreignKey("fk_message_recipient_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_message_recipient_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
