package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class DomainFull(domainId: Long,
                   industryId: Long,
                   name: String,
                   code: String,
                   description: String,
                   industryName : String)

object DomainFull extends Function6[Long, Long, String, String, String, String, DomainFull]
{
    implicit val skillWrites : Writes[DomainFull] = (
            (JsPath \ "domainId").write[Long] and
            (JsPath \ "industryId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "code").write[String] and
            (JsPath \ "description").write[String] and
            (JsPath \ "industryName").write[String]
    )(unlift(DomainFull.unapply))
      
    implicit val skillReads : Reads[DomainFull] = (
          (JsPath \ "domainId").read[Long] and
          (JsPath \ "industryId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "code").read[String] and
          (JsPath \ "description").read[String] and
          (JsPath \ "industryName").read[String]
    )(DomainFull)
    
//    implicit val domainFullReads = Json.reads[DomainFull]
//    implicit val domainFullWrites = Json.writes[DomainFull]
}