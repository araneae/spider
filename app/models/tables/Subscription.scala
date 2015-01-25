package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

/**
 * Table for subscription
 * 
 */

class Subscriptions(tag: Tag) extends Table[Subscription](tag, "subscription") {

  def subscriptionId = column[Long]("subscription_id", O.PrimaryKey, O.AutoInc)
  
  def name = column[String]("name")
  
  def description = column[String]("description")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (subscriptionId.?, name, description, createdUserId, createdAt, updatedUserId, updatedAt) <> (Subscription.tupled, Subscription.unapply)
  
    // foreign keys and indexes
  def uniqueName = index("idx_subscription_on_name", name, unique = true)
  
  def createdBy = foreignKey("fk_subscription_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_subscription_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
