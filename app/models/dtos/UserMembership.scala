package models.dtos

import org.joda.time.DateTime

/*
 * A user can subscribe to multiple memberships. This table stores user to membership mappings.
 */

case class UserMembership(id: Option[Long],
                          userId : Long,
                          membershipId: Long,
                          startDate: DateTime,
                          endDate: DateTime,
                          active: Boolean,
                          description: String)
