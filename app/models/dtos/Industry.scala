package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * Industry signifies the highest level of expertise, like "Software", "Construction", Hospitality", "Medical" etc.
 * 
 */
case class Industry(industryId: Option[Long],
                   code : String,
                   name: String,
                   description: Option[String],
                   createdUserId: Long,
                   createdAt: DateTime = new DateTime(),
                   updatedUserId: Option[Long] = None,
                   updatedAt: Option[DateTime] = None)

object Industry extends Function8[Option[Long], String, String, Option[String], Long, DateTime, Option[Long], Option[DateTime], Industry]
{
    implicit val industryWrites : Writes[Industry] = (
            (JsPath \ "industryId").write[Option[Long]] and
            (JsPath \ "code").write[String] and
            (JsPath \ "name").write[String] and
            (JsPath \ "description").write[Option[String]] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(Industry.unapply))
      
    implicit val industryReads : Reads[Industry] = (
          (JsPath \ "industryId").readNullable[Long] and
          (JsPath \ "code").read[String] and
          (JsPath \ "name").read[String] and
          (JsPath \ "description").readNullable[String] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(Industry)
    
//    implicit val industryReads = Json.reads[Industry]
//    implicit val industryWrites = Json.writes[Industry]
}
