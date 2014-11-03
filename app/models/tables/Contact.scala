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

  def userId = column[Long]("user_id", O.NotNull)
  
  def contactUserId = column[Long]("contact_user_id", O.NotNull)
  
  def status = column[ContactStatus]("status", O.NotNull, O.Default(PENDING))
  
  def token = column[Option[String]]("token", O.Nullable)
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Option[Long]]("updated_user_id", O.Nullable)
  
  def updatedAt = column[Option[DateTime]]("updated_at", O.Nullable)
  
  override def * = (userId, contactUserId, status, token, createdUserId, createdAt, updatedUserId, updatedAt) <> (Contact.tupled, Contact.unapply)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_contact_user_id_contact_user_id", (userId, contactUserId))
  
  def contact = foreignKey("fk_on_contact_contact_user_id", contactUserId, TableQuery[Users])(_.userId)
  
  def createdBy = foreignKey("fk_on_contact_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_contact_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
