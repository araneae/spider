package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * Message recipients
 *  
 */

case class MessageRecipient(userId: Long,
                            messageId: Long,
                            read: Boolean)

object MessageRecipient extends Function3[Long, Long, Boolean, MessageRecipient]
{
    implicit val jsonWrites : Writes[MessageRecipient] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "messageId").write[Long] and
            (JsPath \ "read").write[Boolean]
    )(unlift(MessageRecipient.unapply))
      
    implicit val jsonReads : Reads[MessageRecipient] = (
            (JsPath \ "userId").read[Long] and
            (JsPath \ "messageId").read[Long] and
            (JsPath \ "read").read[Boolean]
    )(MessageRecipient)
}
