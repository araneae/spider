package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.ContactStatus._

case class Contact(userId: Long,
                   contactUserId: Long,
                   status: ContactStatus,
                   token: Option[String])

object Contact extends Function4[Long, Long, ContactStatus, Option[String], Contact]
{
    implicit val contactWrites : Writes[Contact] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "contactUserId").write[Long] and
            (JsPath \ "status").write[ContactStatus] and
            (JsPath \ "token").write[Option[String]]
    )(unlift(Contact.unapply))
      
    implicit val contactReads : Reads[Contact] = (
          (JsPath \ "userId").read[Long] and
          (JsPath \ "contactUserId").read[Long] and
          (JsPath \ "status").read[ContactStatus] and
          (JsPath \ "token").readNullable[String]
    )(Contact)
}
