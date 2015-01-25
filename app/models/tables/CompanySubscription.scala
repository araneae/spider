package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime
import enums.CompanySubscriptionStatusType._

/**
 * Table for company subscription
 * 
 */

class CompanySubscriptions(tag: Tag) extends Table[CompanySubscription](tag, "company_subscription") {

  def companySubscriptionId = column[Long]("company_subscription_id", O.PrimaryKey, O.AutoInc)
  
  def companyId = column[Long]("company_Id")
  
  def subscriptionId = column[Long]("subscription_Id")
  
  def status = column[CompanySubscriptionStatusType]("status")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (companySubscriptionId.?, companyId, subscriptionId, status, createdUserId, createdAt, updatedUserId, updatedAt) <> (CompanySubscription.tupled, CompanySubscription.unapply)
  
    // foreign keys and indexes
  def uniqueCompanySubscription = index("idx_company_subscription_on_company_id_subscription_id", (companyId, subscriptionId), unique = true)
  
  def createdBy = foreignKey("fk_company_subscription_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_company_subscription_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
