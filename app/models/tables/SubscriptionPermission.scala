package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

/**
 * Table for subscription permission
 * 
 */

class SubscriptionPermissions(tag: Tag) extends Table[SubscriptionPermission](tag, "subscription_permission") {

  def subscriptionId = column[Long]("subscription_Id")
  
  def permissionId = column[Long]("permission_Id")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (subscriptionId, permissionId, createdUserId, createdAt, updatedUserId, updatedAt) <> (SubscriptionPermission.tupled, SubscriptionPermission.unapply)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_subscription_permission", (subscriptionId, permissionId))
  
  def subscription = foreignKey("fk_subscription_permission_on_subscription_id", createdUserId, TableQuery[Subscriptions])(_.subscriptionId)
  
  def permission = foreignKey("fk_subscription_permission_on_permission_id", createdUserId, TableQuery[Permissions])(_.permissionId)
  
  def createdBy = foreignKey("fk_subscription_permission_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_subscription_permission_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
