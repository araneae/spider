package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.SkillLevel._
import enums._
import org.joda.time.DateTime

/**
 * Longersection table between user and skill tables.
 * 
 */

case class UserSkill(userId: Long,
                skillId: Long,
                skillLevel: SkillLevel,
                descriptionShort: String,
                descriptionLong: String,
                createdUserId: Long,
                createdAt: DateTime = new DateTime(),
                updatedUserId: Option[Long] = None,
                updatedAt: Option[DateTime] = None)

object UserSkill extends Function9[Long, Long, SkillLevel, String, String, Long, DateTime, Option[Long], Option[DateTime], UserSkill]
{
    implicit val userSkillWrites : Writes[UserSkill] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "skillId").write[Long] and
            (JsPath \ "skillLevel").write[SkillLevel] and
            (JsPath \ "descriptionShort").write[String] and
            (JsPath \ "descriptionLong").write[String] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(UserSkill.unapply))
      
    implicit val userSkillReads : Reads[UserSkill] = (
          (JsPath \ "userId").read[Long] and
          (JsPath \ "skillId").read[Long] and
          (JsPath \ "skillLevel").read[SkillLevel] and
          (JsPath \ "descriptionShort").read[String] and
          (JsPath \ "descriptionLong").read[String] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(UserSkill)
}
