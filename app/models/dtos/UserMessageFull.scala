package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.MessageBoxType._

/**
 * For storing user messages
 *  
 */

case class UserMessageFull(
                   messageId: Long,
                   messageBoxId: Long,
                   messageBoxType: MessageBoxType,
                   subject: String,
                   body: String,
                   sentBy: String,
                   read: Boolean,
                   replied: Boolean,
                   important: Boolean,
                   star: Boolean,
                   outBound: Boolean,
                   createdAt: DateTime
                   )

object UserMessageFull extends Function12[Long, Long, MessageBoxType, String, String, String, Boolean, Boolean, Boolean, Boolean, Boolean, DateTime, UserMessageFull]
{
    implicit val jsonWrites : Writes[UserMessageFull] = (
            (JsPath \ "messageId").write[Long] and
            (JsPath \ "messageBoxId").write[Long] and
            (JsPath \ "messageBoxType").write[MessageBoxType] and
            (JsPath \ "subject").write[String] and
            (JsPath \ "body").write[String] and
            (JsPath \ "sentBy").write[String] and
            (JsPath \ "read").write[Boolean] and
            (JsPath \ "replied").write[Boolean] and
            (JsPath \ "important").write[Boolean] and
            (JsPath \ "star").write[Boolean] and
            (JsPath \ "outBound").write[Boolean] and
            (JsPath \ "createdAt").write[DateTime]
    )(unlift(UserMessageFull.unapply))
      
    implicit val jsonReads : Reads[UserMessageFull] = (
          (JsPath \ "messageId").read[Long] and
          (JsPath \ "messageBoxId").read[Long] and
          (JsPath \ "messageBoxType").read[MessageBoxType] and
          (JsPath \ "subject").read[String] and
            (JsPath \ "body").read[String] and
          (JsPath \ "sentBy").read[String] and
          (JsPath \ "read").read[Boolean] and
          (JsPath \ "replied").read[Boolean] and
          (JsPath \ "important").read[Boolean] and
          (JsPath \ "star").read[Boolean] and
          (JsPath \ "outBound").read[Boolean] and
          (JsPath \ "createdAt").read[DateTime]
    )(UserMessageFull)
}

