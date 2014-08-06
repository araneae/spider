package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * For storing user messages
 *  
 */

case class UserMessageFull(
                   messageId: Long,
                   messageBoxId: Long,
                   subject: String,
                   body: String,
                   sentBy: String,
                   read: Boolean,
                   important: Boolean,
                   star: Boolean
                   )

object UserMessageFull extends Function8[Long, Long, String, String, String, Boolean, Boolean, Boolean, UserMessageFull]
{
    implicit val jsonWrites : Writes[UserMessageFull] = (
            (JsPath \ "messageId").write[Long] and
            (JsPath \ "messageBoxId").write[Long] and
            (JsPath \ "subject").write[String] and
            (JsPath \ "body").write[String] and
            (JsPath \ "sentBy").write[String] and
            (JsPath \ "read").write[Boolean] and
            (JsPath \ "important").write[Boolean] and
            (JsPath \ "star").write[Boolean]
    )(unlift(UserMessageFull.unapply))
      
    implicit val jsonReads : Reads[UserMessageFull] = (
          (JsPath \ "messageId").read[Long] and
          (JsPath \ "messageBoxId").read[Long] and
          (JsPath \ "subject").read[String] and
            (JsPath \ "body").read[String] and
          (JsPath \ "sentBy").read[String] and
          (JsPath \ "read").read[Boolean] and
          (JsPath \ "important").read[Boolean] and
          (JsPath \ "star").read[Boolean] 
    )(UserMessageFull)
}

