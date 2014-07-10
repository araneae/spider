package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._

/**
 * Message recipients
 *  
 */

class MessageRecipients(tag: Tag) extends Table[MessageRecipient](tag, "message_recipient") {

  def userId = column[Long]("user_id", O.NotNull)
  
  def messageId = column[Long]("message_id", O.NotNull)
  
  override def * = (userId, messageId) <> (MessageRecipient.tupled, MessageRecipient.unapply)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_message_recipient", (userId, messageId))
  
  def recipient = foreignKey("fk_on_message_recipient_user_id", userId, TableQuery[Users])(_.id)
  
  def message = foreignKey("fk_on_message_recipient_message_id", messageId, TableQuery[Messages])(_.id)
  
}
