package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._

/**
 * Used by document share POST action
 * 
 */
case class Share(
                 subject: String,
                 message: String,
                 canCopy: Boolean,
                 canShare: Boolean,
                 receivers: List[Connection]
                 )

object Share extends Function5[String, String, Boolean, Boolean, List[Connection], Share]
{
    implicit val shareWrites : Writes[Share] = (
            (JsPath \ "subject").write[String] and
            (JsPath \ "message").write[String] and
            (JsPath \ "canCopy").write[Boolean] and
            (JsPath \ "canShare").write[Boolean] and
            (JsPath \ "receivers").write[List[Connection]]
    )(unlift(Share.unapply))

    implicit val shareReads : Reads[Share] = (
          (JsPath \ "subject").read[String] and
          (JsPath \ "message").read[String] and
          (JsPath \ "canCopy").read[Boolean] and
          (JsPath \ "canShare").read[Boolean] and
          (JsPath \ "receivers").read[List[Connection]]
    )(Share)
}
