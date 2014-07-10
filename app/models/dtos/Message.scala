package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * For storing message
 *  
 */

case class Message(id: Option[Long],
                   messageId: Option[Long],
                   senderId : Option[Long],
                   subject: Option[String],
                   body: Option[String])

object Message extends Function5[Option[Long], Option[Long], Option[Long], Option[String], Option[String], Message]
{
    implicit val jsonWrites : Writes[Message] = (
            (JsPath \ "id").write[Option[Long]] and
            (JsPath \ "messageId").write[Option[Long]] and
            (JsPath \ "senderId").write[Option[Long]] and
            (JsPath \ "subject").write[Option[String]] and
            (JsPath \ "body").write[Option[String]]
    )(unlift(Message.unapply))
      
    implicit val jsonReads : Reads[Message] = (
          (JsPath \ "id").readNullable[Long] and
          (JsPath \ "messageId").readNullable[Long] and
          (JsPath \ "senderId").readNullable[Long] and
          (JsPath \ "subject").readNullable[String] and
          (JsPath \ "body").readNullable[String] 
    )(Message)
}

