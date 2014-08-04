package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._

/**
 * All connections - contacts and advisers
 * 
 */
case class Connection(id: Long,
                 text: String
                 )

object Connection extends Function2[Long, String, Connection]
{
    implicit val connectionWrites : Writes[Connection] = (
            (JsPath \ "id").write[Long] and
            (JsPath \ "text").write[String]
    )(unlift(Connection.unapply))

    implicit val connectionReads : Reads[Connection] = (
          (JsPath \ "id").read[Long] and
          (JsPath \ "text").read[String]
    )(Connection)
}
