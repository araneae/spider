package models.dtos

import enums.SkillLevel._
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class ContactFull(adviserId: Long,
                contactId: Long,
                firstName: String,
                lastName: String,
                email: String
                )

object ContactFull extends Function5[Long, Long, String, String, String, ContactFull]
{
    implicit val contactWrites : Writes[ContactFull] = (
            (JsPath \ "adviserId").write[Long] and
            (JsPath \ "contactId").write[Long] and
            (JsPath \ "firstName").write[String] and
            (JsPath \ "lastName").write[String] and
            (JsPath \ "email").write[String]
    )(unlift(ContactFull.unapply))
      
    implicit val contactReads : Reads[ContactFull] = (
          (JsPath \ "adviserId").read[Long] and
          (JsPath \ "contactId").read[Long] and
          (JsPath \ "firstName").read[String] and
          (JsPath \ "lastName").read[String] and
          (JsPath \ "email").read[String]
    )(ContactFull)
}
