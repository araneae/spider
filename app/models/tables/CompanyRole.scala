package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

/**
 * Table for company roles
 * 
 */

class CompanyRoles(tag: Tag) extends Table[CompanyRole](tag, "company_role") {

  def companyRoleId = column[Long]("company_role_id", O.PrimaryKey, O.AutoInc)
  
  def companyId = column[Long]("company_Id")
  
  def name = column[String]("name")
  
  def description = column[String]("description")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (companyRoleId.?, companyId, name, description, createdUserId, createdAt, updatedUserId, updatedAt) <> (CompanyRole.tupled, CompanyRole.unapply)
  
    // foreign keys and indexes
  def uniqueRoleName = index("idx_company_role_on_company_id_name", (companyId, name), unique = true)
  
  def company = foreignKey("fk_company_role_on_company_id", companyId, TableQuery[Companies])(_.companyId)
  
  def createdBy = foreignKey("fk_company_role_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_company_role_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
