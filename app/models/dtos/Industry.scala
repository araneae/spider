package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * Industry signifies the highest level of expertise, like "Software", "Construction", Hospitality", "Medical" etc.
 * 
 */
case class Industry(id: Option[Long],
                   code : String,
                   name: String,
                   description: String)

object Industry extends Function4[Option[Long], String, String, String, Industry]
{
    implicit val industryWrites : Writes[Industry] = (
            (JsPath \ "id").write[Option[Long]] and
            (JsPath \ "code").write[String] and
            (JsPath \ "name").write[String] and
            (JsPath \ "description").write[String] 
    )(unlift(Industry.unapply))
      
    implicit val industryReads : Reads[Industry] = (
          (JsPath \ "id").readNullable[Long] and
          (JsPath \ "code").read[String] and
          (JsPath \ "name").read[String] and
          (JsPath \ "description").read[String] 
    )(Industry)
    
//    implicit val industryReads = Json.reads[Industry]
//    implicit val industryWrites = Json.writes[Industry]
}
