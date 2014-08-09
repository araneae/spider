package models.tables

import play.api.db.slick.Config.driver.simple._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import models.tables._
import enums.ContactStatus._
import models.dtos._

class Advisers(tag: Tag) extends Table[Adviser](tag, "adviser") {

  def userId = column[Long]("user_id", O.NotNull)
  
  def adviserUserId = column[Long]("adviser_user_id", O.NotNull)
  
  def status = column[ContactStatus]("status", O.NotNull, O.Default(PENDING))
  
  def token = column[String]("token", O.Nullable)
  
  override def * = (userId, adviserUserId, status, token.?) <> (Adviser.tupled, Adviser.unapply)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_adviser_user_id_adviser_user_id", (userId, adviserUserId))
  
  def adviser = foreignKey("fk_on_adviser_adviser_user_id", adviserUserId, TableQuery[Users])(_.userId)
}