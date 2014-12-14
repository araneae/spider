package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * An user can have multiple skills, like Java, C++, ASP.NET, Scala,
 * etc. in Software Industry. Similarly "Carpentry", "Wielding", "Plumbing" etc. in Construction Industry.
 * An skill will fall under an Industry.
 */
case class Skill(skillId: Option[Long],
                 industryId: Long,
                 name: String,
                 description: String,
                 createdUserId: Long,
                 createdAt: DateTime = new DateTime(),
                 updatedUserId: Option[Long] = None,
                 updatedAt: Option[DateTime] = None)

object Skill extends Function8[Option[Long], Long, String, String, Long, DateTime, Option[Long], Option[DateTime], Skill]
{
    implicit val skillWrites : Writes[Skill] = (
            (JsPath \ "skillId").write[Option[Long]] and
            (JsPath \ "industryId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "description").write[String] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(Skill.unapply))
      
    implicit val skillReads : Reads[Skill] = (
          (JsPath \ "skillId").readNullable[Long] and
          (JsPath \ "industryId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "description").read[String] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(Skill)
    
//    implicit val skillReads = Json.reads[Skill]
//    implicit val skillWrites = Json.writes[Skill]
}
