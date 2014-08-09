package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * An user can have multiple skills, like Java, C++, ASP.NET, Scala,
 * etc. in Software Industry. Similarly "Carpentry", "Wielding", "Plumbing" etc. in Construction Industry.
 * An skill will fall under an Industry.
 */
case class Skill(skillId: Option[Long],
                 industryId: Long,
                 name: String,
                 code: String,
                 description: String)

object Skill extends Function5[Option[Long], Long, String, String, String, Skill]
{
    implicit val skillWrites : Writes[Skill] = (
            (JsPath \ "skillId").write[Option[Long]] and
            (JsPath \ "industryId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "code").write[String] and
            (JsPath \ "description").write[String] 
    )(unlift(Skill.unapply))
      
    implicit val skillReads : Reads[Skill] = (
          (JsPath \ "skillId").readNullable[Long] and
          (JsPath \ "industryId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "code").read[String] and
          (JsPath \ "description").read[String] 
    )(Skill)
    
//    implicit val skillReads = Json.reads[Skill]
//    implicit val skillWrites = Json.writes[Skill]
}
