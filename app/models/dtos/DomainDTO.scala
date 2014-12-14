package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class DomainDTO(domainId: Option[Long],
                   industryId: Long,
                   name: String,
                   description: String,
                   industryName : String)

object DomainDTO extends Function5[Option[Long], Long, String, String, String, DomainDTO]
{
    implicit val skillWrites : Writes[DomainDTO] = (
            (JsPath \ "domainId").write[Option[Long]] and
            (JsPath \ "industryId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "description").write[String] and
            (JsPath \ "industryName").write[String]
    )(unlift(DomainDTO.unapply))
      
    implicit val skillReads : Reads[DomainDTO] = (
          (JsPath \ "domainId").readNullable[Long] and
          (JsPath \ "industryId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "description").read[String] and
          (JsPath \ "industryName").read[String]
    )(DomainDTO)
    
//    implicit val domainFullReads = Json.reads[DomainDTO]
//    implicit val domainFullWrites = Json.writes[DomainDTO]
}