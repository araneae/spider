package models.tables

import play.api.db.slick.Config.driver.simple._
import org.joda.time.DateTime
import utils.JodaToSqlMapper._
import models.dtos._

/*
 * A user can subscribe to multiple memberships. This table stores user to membership mappings.
 */

class UserMemberships(tag: Tag) extends Table[UserMembership](tag, "user_membership") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  
  def userId = column[Long]("user_id", O.NotNull)
  
  def membershipId = column[Long]("membership_id", O.NotNull)
  
  def startDate = column[DateTime]("start_date", O.NotNull)
  
  def endDate = column[DateTime]("end_date", O.Nullable)
  
  def active = column[Boolean]("active", O.NotNull, O.Default(true))
  
  def description = column[String]("description", O.Nullable)
  
  override def * = (id.?, userId, membershipId, startDate, endDate, active, description) <> (UserMembership.tupled, UserMembership.unapply)
  
  // foreign keys and indexes
  def user = foreignKey("fk_on_user_id", userId, TableQuery[Users])(_.id)
  
  def membership = foreignKey("fk_on_membership_id", membershipId, TableQuery[Memberships])(_.id)
}