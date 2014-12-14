package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.MessageBoxType._

/**
 * For storing user messages
 *  
 */

case class UserMessageDTO(
                   messageId: Long,
                   messageBoxId: Long,
                   messageBoxType: MessageBoxType,
                   subject: String,
                   body: Option[String],
                   sentBy: String,
                   read: Boolean,
                   replied: Boolean,
                   important: Boolean,
                   star: Boolean,
                   outBound: Boolean,
                   createdAt: DateTime
                   )

object UserMessageDTO extends Function12[Long, Long, MessageBoxType, String, Option[String], String, Boolean, Boolean, Boolean, Boolean, Boolean, DateTime, UserMessageDTO]
{
    implicit val jsonWrites : Writes[UserMessageDTO] = (
            (JsPath \ "messageId").write[Long] and
            (JsPath \ "messageBoxId").write[Long] and
            (JsPath \ "messageBoxType").write[MessageBoxType] and
            (JsPath \ "subject").write[String] and
            (JsPath \ "body").write[Option[String]] and
            (JsPath \ "sentBy").write[String] and
            (JsPath \ "read").write[Boolean] and
            (JsPath \ "replied").write[Boolean] and
            (JsPath \ "important").write[Boolean] and
            (JsPath \ "star").write[Boolean] and
            (JsPath \ "outBound").write[Boolean] and
            (JsPath \ "createdAt").write[DateTime]
    )(unlift(UserMessageDTO.unapply))
      
    implicit val jsonReads : Reads[UserMessageDTO] = (
          (JsPath \ "messageId").read[Long] and
          (JsPath \ "messageBoxId").read[Long] and
          (JsPath \ "messageBoxType").read[MessageBoxType] and
          (JsPath \ "subject").read[String] and
            (JsPath \ "body").readNullable[String] and
          (JsPath \ "sentBy").read[String] and
          (JsPath \ "read").read[Boolean] and
          (JsPath \ "replied").read[Boolean] and
          (JsPath \ "important").read[Boolean] and
          (JsPath \ "star").read[Boolean] and
          (JsPath \ "outBound").read[Boolean] and
          (JsPath \ "createdAt").read[DateTime]
    )(UserMessageDTO)
}

