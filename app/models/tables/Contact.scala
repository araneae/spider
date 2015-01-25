package models.tables

import play.api.db.slick.Config.driver.simple._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import models.tables._
import enums.ContactStatus._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

class Contacts(tag: Tag) extends Table[Contact](tag, "contact") {

  def userId = column[Long]("user_id")
  
  def friendId = column[Long]("friend_id")
  
  def status = column[ContactStatus]("status", O.Default(PENDING))
  
  def token = column[Option[String]]("token")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (userId, friendId, status, token, createdUserId, createdAt, updatedUserId, updatedAt) <> (Contact.tupled, Contact.unapply)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_contact", (userId, friendId))
  
  def contact = foreignKey("fk_contact_on_friend_id", friendId, TableQuery[Users])(_.userId)
  
  def createdBy = foreignKey("fk_contact_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_contact_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
