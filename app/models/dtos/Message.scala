package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * For storing message
 *  
 */

case class Message(messageId: Option[Long],
                   parentMessageId: Option[Long],
                   senderUserId : Long,
                   editable: Boolean,
                   subject: String,
                   body: Option[String])

object Message extends Function6[Option[Long], Option[Long], Long, Boolean, String, Option[String], Message]
{
    implicit val jsonWrites : Writes[Message] = (
            (JsPath \ "messageId").write[Option[Long]] and
            (JsPath \ "parentMessageId").write[Option[Long]] and
            (JsPath \ "senderUserId").write[Long] and
            (JsPath \ "editable").write[Boolean] and
            (JsPath \ "subject").write[String] and
            (JsPath \ "body").write[Option[String]]
    )(unlift(Message.unapply))
      
    implicit val jsonReads : Reads[Message] = (
          (JsPath \ "messageId").readNullable[Long] and
          (JsPath \ "parentMessageId").readNullable[Long] and
          (JsPath \ "senderUserId").read[Long] and
          (JsPath \ "editable").read[Boolean] and
          (JsPath \ "subject").read[String] and
          (JsPath \ "body").readNullable[String] 
    )(Message)
}

