package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * Message recipients
 *  
 */

case class MessageRecipient(userId: Long,
                            messageId: Long,
                            read: Boolean,
                            createdUserId: Long,
                            createdAt: DateTime = new DateTime(),
                            updatedUserId: Option[Long] = None,
                            updatedAt: Option[DateTime] = None)

object MessageRecipient extends Function7[Long, Long, Boolean, Long, DateTime, Option[Long], Option[DateTime], MessageRecipient]
{
    implicit val jsonWrites : Writes[MessageRecipient] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "messageId").write[Long] and
            (JsPath \ "read").write[Boolean] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(MessageRecipient.unapply))
      
    implicit val jsonReads : Reads[MessageRecipient] = (
            (JsPath \ "userId").read[Long] and
            (JsPath \ "messageId").read[Long] and
            (JsPath \ "read").read[Boolean] and
            (JsPath \ "createdUserId").read[Long] and
            (JsPath \ "createdAt").read[DateTime] and
            (JsPath \ "updatedUserId").readNullable[Long] and
            (JsPath \ "updatedAt").readNullable[DateTime]
    )(MessageRecipient)
}
