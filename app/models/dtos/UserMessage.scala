package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * For storing user messages
 *  
 */

case class UserMessage(userId: Long,
                   messageId: Long,
                   messageBoxId : Long,
                   read: Boolean,
                   replied: Boolean,
                   important: Boolean,
                   star: Boolean,
                   createdUserId: Long,
                   createdAt: DateTime = new DateTime(),
                   updatedUserId: Option[Long] = None,
                   updatedAt: Option[DateTime] = None)

object UserMessage extends Function11[Long, Long, Long, Boolean, Boolean, Boolean, Boolean, Long, DateTime, Option[Long], Option[DateTime], UserMessage]
{
    implicit val jsonWrites : Writes[UserMessage] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "messageId").write[Long] and
            (JsPath \ "messageBoxId").write[Long] and
            (JsPath \ "read").write[Boolean] and
            (JsPath \ "replied").write[Boolean] and
            (JsPath \ "important").write[Boolean] and
            (JsPath \ "star").write[Boolean] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(UserMessage.unapply))
      
    implicit val jsonReads : Reads[UserMessage] = (
          (JsPath \ "userId").read[Long] and
          (JsPath \ "messageId").read[Long] and
          (JsPath \ "messageBoxId").read[Long] and
          (JsPath \ "read").read[Boolean] and
          (JsPath \ "replied").read[Boolean] and
          (JsPath \ "important").read[Boolean] and
          (JsPath \ "star").read[Boolean] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(UserMessage)
}

