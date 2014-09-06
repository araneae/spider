package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

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
  
  def body = column[String]("body", O.Nullable, O.DBType("VARCHAR(1024)"))
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Long]("updated_user_id", O.Nullable)
  
  def updatedAt = column[DateTime]("updated_at", O.Nullable)
  
  override def * = (messageId.?, parentMessageId.?, senderUserId, editable, subject, body.?, createdUserId, createdAt, updatedUserId.?, updatedAt.?) <> (Message.tupled, Message.unapply)
  
  // foreign keys and indexes
  def sender = foreignKey("fk_on_message_sender_user_id", senderUserId, TableQuery[Users])(_.userId)
  
  def parentMessage = foreignKey("fk_on_message_message_id", parentMessageId, TableQuery[Messages])(_.messageId)
  
  def createdBy = foreignKey("fk_on_message_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_message_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
