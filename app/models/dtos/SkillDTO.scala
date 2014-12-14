package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class SkillDTO(skillId: Long,
                   industryId: Long,
                   name: String,
                   description: String,
                   industryName : String)

object SkillDTO extends Function5[Long, Long, String, String, String, SkillDTO]
{
    implicit val skillWrites : Writes[SkillDTO] = (
            (JsPath \ "skillId").write[Long] and
            (JsPath \ "industryId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "description").write[String] and
            (JsPath \ "industryName").write[String]
    )(unlift(SkillDTO.unapply))
      
    implicit val skillReads : Reads[SkillDTO] = (
          (JsPath \ "skillId").read[Long] and
          (JsPath \ "industryId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "description").read[String] and
          (JsPath \ "industryName").read[String]
    )(SkillDTO)
    
//    implicit val skillFullReads = Json.reads[SkillDTO]
//    implicit val skillFullWrites = Json.writes[SkillDTO]
}
