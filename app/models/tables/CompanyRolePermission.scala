package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

/**
 * Table for company role permissions
 * 
 */

class CompanyRolePermissions(tag: Tag) extends Table[CompanyRolePermission](tag, "company_role_permission") {

  def companyRolePermissionId = column[Long]("company_role_permission_id", O.PrimaryKey, O.AutoInc)
  
  def companyRoleId = column[Long]("company_role_Id")
  
  def companySubscriptionId = column[Long]("company_subscription_Id")
  
  def permissionId = column[Long]("permission_Id")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (companyRolePermissionId.?, companyRoleId, companySubscriptionId, permissionId, createdUserId, createdAt, updatedUserId, updatedAt) <> (CompanyRolePermission.tupled, CompanyRolePermission.unapply)
  
    // foreign keys and indexes
  def uniqueCompanyRoleSubscriptionPermission = index("idx_company_role_permission_on_comp_id_sub_id_perm_id", (companyRoleId, companySubscriptionId, permissionId), unique = true)
  
  def companyRole = foreignKey("fk_company_role_permission_on_company_role_id", companyRoleId, TableQuery[CompanyRoles])(_.companyRoleId)
  
  def createdBy = foreignKey("fk_company_role_permission_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_company_role_permission_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
