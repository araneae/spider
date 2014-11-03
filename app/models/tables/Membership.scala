package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

/**
 * This class represents various membership levels available in the system.
 * Available Memberships are:
 *          Limited Membership -. Primarily the developers (Job Seekers)
 *          Recruiter          -  Job Providers
 *          Hiring Manager     -  ??
 */

class Memberships(tag: Tag) extends Table[Membership](tag, "membership") {

  def membershipId = column[Long]("membership_id", O.PrimaryKey, O.AutoInc)
  
  def name = column[String]("name", O.NotNull)
  
  def description = column[Option[String]]("description", O.Nullable)
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Option[Long]]("updated_user_id", O.Nullable)
  
  def updatedAt = column[Option[DateTime]]("updated_at", O.Nullable)
  
  override def * = (membershipId.?, name, description, createdUserId, createdAt, updatedUserId, updatedAt) <> (Membership.tupled, Membership.unapply)
  
  // foreign keys and indexes
  def createdBy = foreignKey("fk_on_membership_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_membership_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}

