package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * Message recipients
 *  
 */

case class MessageRecipient(userId: Long,
                            messageId: Long)

object MessageRecipient extends Function2[Long, Long, MessageRecipient]
{
    implicit val jsonWrites : Writes[MessageRecipient] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "messageId").write[Long]
    )(unlift(MessageRecipient.unapply))
      
    implicit val jsonReads : Reads[MessageRecipient] = (
            (JsPath \ "userId").read[Long] and
            (JsPath \ "messageId").read[Long]
    )(MessageRecipient)
}
