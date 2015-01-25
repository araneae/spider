package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

/**
 * Table for company user roles
 * 
 */

class CompanyUserRoles(tag: Tag) extends Table[CompanyUserRole](tag, "company_user_role") {

  def companyUserRoleId = column[Long]("company_user_role_id", O.PrimaryKey, O.AutoInc)
  
  def companyUserId = column[Long]("company_user_Id")
  
  def companyRoleId = column[Long]("company_role_Id")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (companyUserRoleId.?, companyUserId, companyRoleId, createdUserId, createdAt, updatedUserId, updatedAt) <> (CompanyUserRole.tupled, CompanyUserRole.unapply)
  
    // foreign keys and indexes
  def uniqueUserRole = index("idx_company_user_role_on_company_user_id_company_role_id", (companyUserId, companyRoleId), unique = true)
  
  def companyUser = foreignKey("fk_company_user_role_on_company_user_id", companyUserId, TableQuery[CompanyUsers])(_.companyUserId)
  
  def companyRole = foreignKey("fk_company_user_role_on_company_role_id", companyRoleId, TableQuery[CompanyRoles])(_.companyRoleId)
  
  def createdBy = foreignKey("fk_company_user_role_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_company_user_role_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
