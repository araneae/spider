package models.tables

import play.api.db.slick.Config.driver.simple._
import enums.MessageBoxType._
import models.dtos._

/**
 * Message box
 * 
 */


class MessageBoxs(tag: Tag) extends Table[MessageBox](tag, "message_box") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  
  def userId = column[Long]("user_id", O.NotNull)
  
  def messageBoxType = column[MessageBoxType]("message_box_type", O.NotNull)
  
  def name = column[String]("name", O.NotNull)
  
  override def * = (id.?, userId, messageBoxType, name) <> (MessageBox.tupled, MessageBox.unapply)
  
  // foreign keys and indexes
  def owner = foreignKey("fk_on_message_box_user_id", userId, TableQuery[Users])(_.id)
}
