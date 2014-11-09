package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * For contact invitation
 *  
 */

case class ContactInvite(
                   contactUserId : Long,
                   subject: String,
                   message: String
                  )

object ContactInvite extends Function3[Long, String, String, ContactInvite]
{
    implicit val contactInviteReads = Json.reads[ContactInvite]
    implicit val contactInviteWrites = Json.writes[ContactInvite]
}

