package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * For storing message
 *  
 */

case class Message(messageId: Option[Long],
                   parentMessageId: Option[Long],
                   senderUserId : Long,
                   editable: Boolean,
                   subject: String,
                   body: Option[String],
                   createdUserId: Long,
                   createdAt: DateTime = new DateTime(),
                   updatedUserId: Option[Long] = None,
                   updatedAt: Option[DateTime] = None)

object Message extends Function10[Option[Long], Option[Long], Long, Boolean, String, Option[String], Long, DateTime, Option[Long], Option[DateTime], Message]
{
    implicit val jsonWrites : Writes[Message] = (
            (JsPath \ "messageId").write[Option[Long]] and
            (JsPath \ "parentMessageId").write[Option[Long]] and
            (JsPath \ "senderUserId").write[Long] and
            (JsPath \ "editable").write[Boolean] and
            (JsPath \ "subject").write[String] and
            (JsPath \ "body").write[Option[String]] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(Message.unapply))
      
    implicit val jsonReads : Reads[Message] = (
          (JsPath \ "messageId").readNullable[Long] and
          (JsPath \ "parentMessageId").readNullable[Long] and
          (JsPath \ "senderUserId").read[Long] and
          (JsPath \ "editable").read[Boolean] and
          (JsPath \ "subject").read[String] and
          (JsPath \ "body").readNullable[String] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(Message)
}

