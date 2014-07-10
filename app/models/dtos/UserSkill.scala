package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.SkillLevel._
import enums._

/**
 * Longersection table between user and skill tables.
 * 
 */

case class UserSkill(userId: Long,
                skillId: Long,
                skillLevel: SkillLevel,
                descriptionShort: String,
                descriptionLong: String
                )

object UserSkill extends Function5[Long, Long, SkillLevel, String, String, UserSkill]
{
    implicit val userSkillWrites : Writes[UserSkill] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "skillId").write[Long] and
            (JsPath \ "skillLevel").write[SkillLevel] and
            (JsPath \ "descriptionShort").write[String] and
            (JsPath \ "descriptionLong").write[String]
    )(unlift(UserSkill.unapply))
      
    implicit val userSkillReads : Reads[UserSkill] = (
          (JsPath \ "userId").read[Long] and
          (JsPath \ "skillId").read[Long] and
          (JsPath \ "skillLevel").read[SkillLevel] and
          (JsPath \ "descriptionShort").read[String] and
          (JsPath \ "descriptionLong").read[String]
    )(UserSkill)
}
