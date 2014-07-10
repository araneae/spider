package models.dtos

import enums.SkillLevel._
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class AdviserFull(adviserId: Long,
                contactId: Long,
                firstName: String,
                lastName: String,
                email: String
                )

object AdviserFull extends Function5[Long, Long, String, String, String, AdviserFull]
{
    implicit val contactWrites : Writes[AdviserFull] = (
            (JsPath \ "adviserId").write[Long] and
            (JsPath \ "contactId").write[Long] and
            (JsPath \ "firstName").write[String] and
            (JsPath \ "lastName").write[String] and
            (JsPath \ "email").write[String]
    )(unlift(AdviserFull.unapply))
      
    implicit val contactReads : Reads[AdviserFull] = (
          (JsPath \ "adviserId").read[Long] and
          (JsPath \ "contactId").read[Long] and
          (JsPath \ "firstName").read[String] and
          (JsPath \ "lastName").read[String] and
          (JsPath \ "email").read[String]
    )(AdviserFull)
}
