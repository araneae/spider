package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

/**
 * Table for permission
 * 
 */

class Permissions(tag: Tag) extends Table[Permission](tag, "permission") {

  def permissionId = column[Long]("permission_id", O.PrimaryKey, O.AutoInc)
  
  def code = column[String]("code")
  
  def name = column[String]("name")
  
  def description = column[String]("description")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (permissionId.?, code, name, description, createdUserId, createdAt, updatedUserId, updatedAt) <> (Permission.tupled, Permission.unapply)
  
    // foreign keys and indexes
  def uniqueCode = index("idx_permission_on_code", code, unique = true)
  
  def uniqueName = index("idx_permission_on_name", name, unique = true)
  
  def createdBy = foreignKey("fk_permission_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_permission_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
