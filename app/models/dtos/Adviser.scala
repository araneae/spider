package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.ContactStatus._

case class Adviser(userId: Long,
                   adviserUserId: Long,
                   status: ContactStatus,
                   token: Option[String])

object Adviser extends Function4[Long, Long, ContactStatus, Option[String], Adviser]
{
    implicit val adviserWrites : Writes[Adviser] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "adviserUserId").write[Long] and
            (JsPath \ "status").write[ContactStatus] and
            (JsPath \ "token").write[Option[String]]
    )(unlift(Adviser.unapply))
      
    implicit val adviserReads : Reads[Adviser] = (
          (JsPath \ "userId").read[Long] and
          (JsPath \ "adviserUserId").read[Long] and
          (JsPath \ "status").read[ContactStatus] and
          (JsPath \ "token").readNullable[String]
    )(Adviser)
}