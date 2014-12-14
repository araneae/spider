package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.MessageBoxType._
import org.joda.time.DateTime

/**
 * Message box
 * 
 */

case class MessageBoxDTO(
                   messageBoxId: Long,
                   messageBoxType: MessageBoxType,
                   name :String,
                   createdAt: DateTime,
                   messageCount: Long)

object MessageBoxDTO extends Function5[Long, MessageBoxType, String, DateTime, Long, MessageBoxDTO]
{
    implicit val jsonWrites : Writes[MessageBoxDTO] = (
            (JsPath \ "messageBoxId").write[Long] and
            (JsPath \ "messageBoxType").write[MessageBoxType] and
            (JsPath \ "name").write[String] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "messageCount").write[Long]
    )(unlift(MessageBoxDTO.unapply))
      
    implicit val jsonReads : Reads[MessageBoxDTO] = (
          (JsPath \ "messageBoxId").read[Long] and
          (JsPath \ "messageBoxType").read[MessageBoxType] and
          (JsPath \ "name").read[String] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "messageCount").read[Long]
    )(MessageBoxDTO)
}
