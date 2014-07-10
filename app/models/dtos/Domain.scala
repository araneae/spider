package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * A domain is a somewhat broader definition of expertise area, like "Telecom", "Retail", "Insurance"
 * etc. in Software Industry. An user can one or more or none experience in domains. A domain will fall
 * under an Industry.
 */
case class Domain(id: Option[Long],
                   industryId: Long,
                   name: String,
                   code: String,
                   description: String)

object Domain extends Function5[Option[Long], Long, String, String, String, Domain]
{
    implicit val domainWrites : Writes[Domain] = (
            (JsPath \ "id").write[Option[Long]] and
            (JsPath \ "industryId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "code").write[String] and
            (JsPath \ "description").write[String] 
    )(unlift(Domain.unapply))
      
    implicit val domainReads : Reads[Domain] = (
          (JsPath \ "id").readNullable[Long] and
          (JsPath \ "industryId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "code").read[String] and
          (JsPath \ "description").read[String] 
    )(Domain)
    
//    implicit val domainReads = Json.reads[Domain]
//    implicit val domainWrites = Json.writes[Domain]
}
