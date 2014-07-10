package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._

/**
 * For storing message
 *  
 */

class Messages(tag: Tag) extends Table[Message](tag, "message") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  
  def messageId = column[Long]("message_id", O.Nullable)
  
  def senderId = column[Long]("sender_id", O.Nullable)
  
  def subject = column[String]("subject", O.Nullable)
  
  def body = column[String]("body", O.Nullable)
  
  override def * = (id.?, messageId.?, senderId.?, subject.?, body.?) <> (Message.tupled, Message.unapply)
  
  // foreign keys and indexes
  def sender = foreignKey("fk_on_message_sender_id", senderId, TableQuery[Users])(_.id)
  
  def message = foreignKey("fk_on_message_message_id", senderId, TableQuery[Messages])(_.id)
}
