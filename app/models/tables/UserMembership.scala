package models.tables

import play.api.db.slick.Config.driver.simple._
import org.joda.time.DateTime
import utils.JodaToSqlMapper._
import models.dtos._

/*
 * A user can subscribe to multiple memberships. This table stores user to membership mappings.
 */

class UserMemberships(tag: Tag) extends Table[UserMembership](tag, "user_membership") {

  def userMembershipId = column[Long]("user_membership_id", O.PrimaryKey, O.AutoInc)
  
  def userId = column[Long]("user_id", O.NotNull)
  
  def membershipId = column[Long]("membership_id", O.NotNull)
  
  def startDate = column[DateTime]("start_date", O.NotNull)
  
  def endDate = column[Option[DateTime]]("end_date", O.Nullable)
  
  def active = column[Boolean]("active", O.NotNull, O.Default(true))
  
  def description = column[Option[String]]("description", O.Nullable)
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Option[Long]]("updated_user_id", O.Nullable)
  
  def updatedAt = column[Option[DateTime]]("updated_at", O.Nullable)
  
  override def * = (userMembershipId.?, userId, membershipId, startDate, endDate, active, description, createdUserId, createdAt, updatedUserId, updatedAt) <> (UserMembership.tupled, UserMembership.unapply)
  
  // foreign keys and indexes
  def user = foreignKey("fk_on_user_id", userId, TableQuery[Users])(_.userId)
  
  def membership = foreignKey("fk_on_membership_id", membershipId, TableQuery[Memberships])(_.membershipId)
  
  def createdBy = foreignKey("fk_on_user_membership_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_user_membership_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}