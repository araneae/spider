package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * A domain is a somewhat broader definition of expertise area, like "Telecom", "Retail", "Insurance"
 * etc. in Software Industry. An user can one or more or none experience in domains. A domain will fall
 * under an Industry.
 */
case class Domain(domainId: Option[Long],
                   industryId: Long,
                   name: String,
                   description: String,
                   createdUserId: Long,
                   createdAt: DateTime = new DateTime(),
                   updatedUserId: Option[Long] = None,
                   updatedAt: Option[DateTime] = None)

object Domain extends Function8[Option[Long], Long, String, String, Long, DateTime, Option[Long], Option[DateTime], Domain]
{
    implicit val domainWrites : Writes[Domain] = (
            (JsPath \ "domainId").write[Option[Long]] and
            (JsPath \ "industryId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "description").write[String] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(Domain.unapply))
      
    implicit val domainReads : Reads[Domain] = (
          (JsPath \ "domainId").readNullable[Long] and
          (JsPath \ "industryId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "description").read[String] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(Domain)
    
//    implicit val domainReads = Json.reads[Domain]
//    implicit val domainWrites = Json.writes[Domain]
}
