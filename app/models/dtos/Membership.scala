package models.dtos

/**
 * This class represents various membership levels available in the system.
 * Available Memberships are:
 *          Limited Membership -. Primarily the developers (Job Seekers)
 *          Recruiter          -  Job Providers
 *          Hiring Manager     -  ??
 */

case class Membership(id: Option[Long], 
                       name: String,
                       description: String)
