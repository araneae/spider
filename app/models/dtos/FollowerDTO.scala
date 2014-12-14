package models.dtos

import enums.SkillLevel._
import play.api.libs.functional.syntax._
import play.api.libs.json._

case class FollowerDTO(subjectId: Long,
                followerId: Long,
                firstName: String,
                lastName: String,
                email: String
                )

object FollowerDTO extends Function5[Long, Long, String, String, String, FollowerDTO]
{
    implicit val contactWrites : Writes[FollowerDTO] = (
            (JsPath \ "subjectId").write[Long] and
            (JsPath \ "followerId").write[Long] and
            (JsPath \ "firstName").write[String] and
            (JsPath \ "lastName").write[String] and
            (JsPath \ "email").write[String]
    )(unlift(FollowerDTO.unapply))
      
    implicit val contactReads : Reads[FollowerDTO] = (
          (JsPath \ "subjectId").read[Long] and
          (JsPath \ "followerId").read[Long] and
          (JsPath \ "firstName").read[String] and
          (JsPath \ "lastName").read[String] and
          (JsPath \ "email").read[String]
    )(FollowerDTO)
}
