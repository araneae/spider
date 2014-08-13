package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.ContactStatus._
import org.joda.time.DateTime

case class Contact(userId: Long,
                   contactUserId: Long,
                   status: ContactStatus,
                   token: Option[String],
                   createdUserId: Long,
                   createdAt: DateTime = new DateTime(),
                   updatedUserId: Option[Long] = None,
                   updatedAt: Option[DateTime] = None)

object Contact extends Function8[Long, Long, ContactStatus, Option[String], Long, DateTime, Option[Long], Option[DateTime], Contact]
{
    implicit val contactWrites : Writes[Contact] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "contactUserId").write[Long] and
            (JsPath \ "status").write[ContactStatus] and
            (JsPath \ "token").write[Option[String]] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
            
    )(unlift(Contact.unapply))
      
    implicit val contactReads : Reads[Contact] = (
          (JsPath \ "userId").read[Long] and
          (JsPath \ "contactUserId").read[Long] and
          (JsPath \ "status").read[ContactStatus] and
          (JsPath \ "token").readNullable[String] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(Contact)
}
