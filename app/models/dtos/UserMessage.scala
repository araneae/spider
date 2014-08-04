package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * For storing user messages
 *  
 */

case class UserMessage(userId: Long,
                   messageId: Long,
                   messageBoxId : Long,
                   read: Boolean,
                   important: Boolean,
                   star: Boolean
                   )

object UserMessage extends Function6[Long, Long, Long, Boolean, Boolean, Boolean, UserMessage]
{
    implicit val jsonWrites : Writes[UserMessage] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "messageId").write[Long] and
            (JsPath \ "parentMessageId").write[Long] and
            (JsPath \ "read").write[Boolean] and
            (JsPath \ "important").write[Boolean] and
            (JsPath \ "star").write[Boolean]
    )(unlift(UserMessage.unapply))
      
    implicit val jsonReads : Reads[UserMessage] = (
          (JsPath \ "userId").read[Long] and
          (JsPath \ "messageId").read[Long] and
          (JsPath \ "parentMessageId").read[Long] and
          (JsPath \ "read").read[Boolean] and
          (JsPath \ "important").read[Boolean] and
          (JsPath \ "star").read[Boolean] 
    )(UserMessage)
}

