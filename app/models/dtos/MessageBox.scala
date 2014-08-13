package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.MessageBoxType._
import org.joda.time.DateTime

/**
 * Message box
 * 
 */

case class MessageBox(messageBoxId: Option[Long],
                   userId: Long,
                   messageBoxType: MessageBoxType,
                   name :String,
                   createdUserId: Long,
                   createdAt: DateTime = new DateTime(),
                   updatedUserId: Option[Long] = None,
                   updatedAt: Option[DateTime] = None)

object MessageBox extends Function8[Option[Long], Long, MessageBoxType, String, Long, DateTime, Option[Long], Option[DateTime], MessageBox]
{
    implicit val jsonWrites : Writes[MessageBox] = (
            (JsPath \ "messageBoxId").write[Option[Long]] and
            (JsPath \ "userId").write[Long] and
            (JsPath \ "messageBoxType").write[MessageBoxType] and
            (JsPath \ "name").write[String] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(MessageBox.unapply))
      
    implicit val jsonReads : Reads[MessageBox] = (
          (JsPath \ "messageBoxId").readNullable[Long] and
          (JsPath \ "userId").read[Long] and
          (JsPath \ "messageBoxType").read[MessageBoxType] and
          (JsPath \ "name").read[String] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(MessageBox)
}
