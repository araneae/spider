package models.tables

import play.api.db.slick.Config.driver.simple._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import models.tables._
import enums.ContactStatus._
import models.dtos._

class Contacts(tag: Tag) extends Table[Contact](tag, "contact") {

  def userId = column[Long]("user_id", O.NotNull)
  
  def contactUserId = column[Long]("contact_user_id", O.NotNull)
  
  def status = column[ContactStatus]("status", O.NotNull, O.Default(PENDING))
  
  def token = column[String]("token", O.Nullable)
  
  override def * = (userId, contactUserId, status, token.?) <> (Contact.tupled, Contact.unapply)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_contact_user_id_contact_user_id", (userId, contactUserId))
  
  def contact = foreignKey("fk_on_contact_contact_user_id", contactUserId, TableQuery[Users])(_.userId)
}
