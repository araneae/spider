package models.tables

import play.api.db.slick.Config.driver.simple._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import models.tables._
import enums.ContactStatus._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

class Advisers(tag: Tag) extends Table[Adviser](tag, "adviser") {

  def userId = column[Long]("user_id", O.NotNull)
  
  def adviserUserId = column[Long]("adviser_user_id", O.NotNull)
  
  def status = column[ContactStatus]("status", O.NotNull, O.Default(PENDING))
  
  def token = column[String]("token", O.Nullable)
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Long]("updated_user_id", O.Nullable)
  
  def updatedAt = column[DateTime]("updated_at", O.Nullable)
  
  override def * = (userId, adviserUserId, status, token.?, createdUserId, createdAt, updatedUserId.?, updatedAt.?) <> (Adviser.tupled, Adviser.unapply)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_adviser_user_id_adviser_user_id", (userId, adviserUserId))
  
  def adviser = foreignKey("fk_on_adviser_adviser_user_id", adviserUserId, TableQuery[Users])(_.userId)
  
  def createdBy = foreignKey("fk_on_adviser_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_adviser_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}