package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._

/**
 * For storing message
 *  
 */

class Messages(tag: Tag) extends Table[Message](tag, "message") {

  def messageId = column[Long]("message_id", O.PrimaryKey, O.AutoInc)
  
  def parentMessageId = column[Long]("parent_message_id", O.Nullable)
  
  def senderUserId = column[Long]("sender_user_id", O.NotNull)
  
  def editable = column[Boolean]("editable", O.NotNull, O.Default(true))
  
  def subject = column[String]("subject", O.NotNull)
  
  def body = column[String]("body", O.Nullable)
  
  override def * = (messageId.?, parentMessageId.?, senderUserId, editable, subject, body.?) <> (Message.tupled, Message.unapply)
  
  // foreign keys and indexes
  def sender = foreignKey("fk_on_message_sender_user_id", senderUserId, TableQuery[Users])(_.id)
  
  def parentMessage = foreignKey("fk_on_message_message_id", parentMessageId, TableQuery[Messages])(_.messageId)
}
