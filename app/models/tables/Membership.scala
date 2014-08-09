package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._

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
  
  def description = column[String]("description", O.Nullable)
  
  override def * = (membershipId.?, name, description) <> (Membership.tupled, Membership.unapply)
  
  // foreign keys and indexes
}

