package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class SkillFull(skillId: Long,
                   industryId: Long,
                   name: String,
                   code: String,
                   description: Option[String],
                   industryName : String)

object SkillFull extends Function6[Long, Long, String, String, Option[String], String, SkillFull]
{
    implicit val skillWrites : Writes[SkillFull] = (
            (JsPath \ "skillId").write[Long] and
            (JsPath \ "industryId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "code").write[String] and
            (JsPath \ "description").write[Option[String]] and
            (JsPath \ "industryName").write[String]
    )(unlift(SkillFull.unapply))
      
    implicit val skillReads : Reads[SkillFull] = (
          (JsPath \ "skillId").read[Long] and
          (JsPath \ "industryId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "code").read[String] and
          (JsPath \ "description").readNullable[String] and
          (JsPath \ "industryName").read[String]
    )(SkillFull)
    
//    implicit val skillFullReads = Json.reads[SkillFull]
//    implicit val skillFullWrites = Json.writes[SkillFull]
}
