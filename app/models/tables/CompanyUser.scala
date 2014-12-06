package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime
import enums.CompanyUserStatusType._
import enums.CompanyUserType._

/**
 * Company Users
 * 
 */

class CompanyUsers(tag: Tag) extends Table[CompanyUser](tag, "company_user") {

  def companyUserId = column[Long]("company_id", O.PrimaryKey, O.AutoInc)
  
  def companyId = column[Long]("company_id", O.NotNull)
  
  def firstName = column[String]("first_name", O.NotNull)
  
  def middleName = column[Option[String]]("middle_name", O.Nullable)
  
  def lastName = column[String]("last_name", O.NotNull)
  
  def email = column[String]("email", O.NotNull)
  
  def status = column[CompanyUserStatusType]("status", O.NotNull)
  
  def userType = column[CompanyUserType]("user_type", O.NotNull)
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Option[Long]]("updated_user_id", O.Nullable)
  
  def updatedAt = column[Option[DateTime]]("updated_at", O.Nullable)
  
  override def * = (companyUserId.?, companyId, firstName, middleName, lastName, email, status, userType, createdUserId, createdAt, updatedUserId, updatedAt) <> (CompanyUser.tupled, CompanyUser.unapply)
  
  // foreign keys and indexes
  def uniqueEmail = index("idx_company_user_on_email_unique", (companyId, email), unique = true)
  
  def company = foreignKey("fk_company_user_company_id", companyId, TableQuery[Companies])(_.companyId)
  
  def createdBy = foreignKey("fk_company_user_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_company_user_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
