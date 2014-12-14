package models.dtos

import enums.SkillLevel._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.ContactStatus._

case class ContactDTO(
                contactId: Long,
                firstName: String,
                lastName: String,
                email: String,
                status: ContactStatus
                )

object ContactDTO extends Function5[Long, String, String, String, ContactStatus, ContactDTO]
{
    implicit val contactWrites : Writes[ContactDTO] = (
            (JsPath \ "contactId").write[Long] and
            (JsPath \ "firstName").write[String] and
            (JsPath \ "lastName").write[String] and
            (JsPath \ "email").write[String] and
            (JsPath \ "status").write[ContactStatus]
    )(unlift(ContactDTO.unapply))
      
    implicit val contactReads : Reads[ContactDTO] = (
          (JsPath \ "contactId").read[Long] and
          (JsPath \ "firstName").read[String] and
          (JsPath \ "lastName").read[String] and
          (JsPath \ "email").read[String] and
          (JsPath \ "status").read[ContactStatus]
    )(ContactDTO)
}
