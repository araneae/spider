package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.MessageBoxType._

/**
 * Message box
 * 
 */

case class MessageBox(messageBoxId: Option[Long],
                   userId: Long,
                   messageBoxType: MessageBoxType,
                   name :String)

object MessageBox extends Function4[Option[Long], Long, MessageBoxType, String, MessageBox]
{
    implicit val jsonWrites : Writes[MessageBox] = (
            (JsPath \ "messageBoxId").write[Option[Long]] and
            (JsPath \ "userId").write[Long] and
            (JsPath \ "messageBoxType").write[MessageBoxType] and
            (JsPath \ "name").write[String]
    )(unlift(MessageBox.unapply))
      
    implicit val jsonReads : Reads[MessageBox] = (
          (JsPath \ "messageBoxId").readNullable[Long] and
          (JsPath \ "userId").read[Long] and
          (JsPath \ "messageBoxType").read[MessageBoxType] and
          (JsPath \ "name").read[String] 
    )(MessageBox)
}
