package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._

/**
 * All connections - contacts and followers
 * 
 */
case class ConnectionDTO(id: Long,
                 text: String
                 )

object ConnectionDTO extends Function2[Long, String, ConnectionDTO]
{
    implicit val connectionWrites : Writes[ConnectionDTO] = (
            (JsPath \ "id").write[Long] and
            (JsPath \ "text").write[String]
    )(unlift(ConnectionDTO.unapply))

    implicit val connectionReads : Reads[ConnectionDTO] = (
          (JsPath \ "id").read[Long] and
          (JsPath \ "text").read[String]
    )(ConnectionDTO)
}
