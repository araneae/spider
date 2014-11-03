package models.dtos

import org.joda.time.DateTime

/**
 * This class represents various membership levels available in the system.
 * Available Memberships are:
 *          Limited Membership -. Primarily the developers (Job Seekers)
 *          Recruiter          -  Job Providers
 *          Hiring Manager     -  ??
 */

case class Membership(membershipId: Option[Long], 
                       name: String,
                       description: Option[String],
                       createdUserId: Long,
                       createdAt: DateTime = new DateTime(),
                       updatedUserId: Option[Long] = None,
                       updatedAt: Option[DateTime] = None)
