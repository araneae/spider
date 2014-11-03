package models.dtos

import org.joda.time.DateTime

/*
 * A user can subscribe to multiple memberships. This table stores user to membership mappings.
 */

case class UserMembership(userMembershipId: Option[Long],
                          userId : Long,
                          membershipId: Long,
                          startDate: DateTime,
                          endDate: Option[DateTime],
                          active: Boolean,
                          description: Option[String],
                          createdUserId: Long,
                          createdAt: DateTime = new DateTime(),
                          updatedUserId: Option[Long] = None,
                          updatedAt: Option[DateTime] = None)
