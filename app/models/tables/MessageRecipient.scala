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

  def userId = column[Long]("user_id", O.NotNull)
  
  def messageId = column[Long]("message_id", O.NotNull)
  
  def read = column[Boolean]("read", O.NotNull)
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Long]("updated_user_id", O.Nullable)
  
  def updatedAt = column[DateTime]("updated_at", O.Nullable)
  
  override def * = (userId, messageId, read, createdUserId, createdAt, updatedUserId.?, updatedAt.?) <> (MessageRecipient.tupled, MessageRecipient.unapply)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_message_recipient", (userId, messageId))
  
  def recipient = foreignKey("fk_on_message_recipient_user_id", userId, TableQuery[Users])(_.userId)
  
  def message = foreignKey("fk_on_message_recipient_message_id", messageId, TableQuery[Messages])(_.messageId)
  
  def createdBy = foreignKey("fk_on_message_recipient_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_message_recipient_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
