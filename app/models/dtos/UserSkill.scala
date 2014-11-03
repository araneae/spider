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
                descriptionShort: Option[String],
                descriptionLong: Option[String],
                createdUserId: Long,
                createdAt: DateTime = new DateTime(),
                updatedUserId: Option[Long] = None,
                updatedAt: Option[DateTime] = None)

object UserSkill extends Function9[Long, Long, SkillLevel, Option[String], Option[String], Long, DateTime, Option[Long], Option[DateTime], UserSkill]
{
    implicit val userSkillWrites : Writes[UserSkill] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "skillId").write[Long] and
            (JsPath \ "skillLevel").write[SkillLevel] and
            (JsPath \ "descriptionShort").write[Option[String]] and
            (JsPath \ "descriptionLong").write[Option[String]] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(UserSkill.unapply))
      
    implicit val userSkillReads : Reads[UserSkill] = (
          (JsPath \ "userId").read[Long] and
          (JsPath \ "skillId").read[Long] and
          (JsPath \ "skillLevel").read[SkillLevel] and
          (JsPath \ "descriptionShort").readNullable[String] and
          (JsPath \ "descriptionLong").readNullable[String] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(UserSkill)
}
