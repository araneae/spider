package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * For contact invitation by email
 *  
 */

case class EmailSignupDTO(
                   email : String
                  )

object EmailSignupDTO extends Function1[String, EmailSignupDTO]
{
    implicit val contactInviteReads = Json.reads[EmailSignupDTO]
    implicit val contactInviteWrites = Json.writes[EmailSignupDTO]
}

