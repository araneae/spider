package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.ContactStatus._
import org.joda.time.DateTime

case class Adviser(userId: Long,
                   adviserUserId: Long,
                   status: ContactStatus,
                   token: Option[String],
                   createdUserId: Long,
                   createdAt: DateTime = new DateTime(),
                   updatedUserId: Option[Long] = None,
                   updatedAt: Option[DateTime] = None)

object Adviser extends Function8[Long, Long, ContactStatus, Option[String], Long, DateTime, Option[Long], Option[DateTime], Adviser]
{
    implicit val adviserWrites : Writes[Adviser] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "adviserUserId").write[Long] and
            (JsPath \ "status").write[ContactStatus] and
            (JsPath \ "token").write[Option[String]] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(Adviser.unapply))
      
    implicit val adviserReads : Reads[Adviser] = (
          (JsPath \ "userId").read[Long] and
          (JsPath \ "adviserUserId").read[Long] and
          (JsPath \ "status").read[ContactStatus] and
          (JsPath \ "token").readNullable[String] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(Adviser)
}