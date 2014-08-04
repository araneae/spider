package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._

/**
 * Used by document share POST action
 * 
 */
case class Share(message: String,
                 canEdit: Boolean,
                 receivers: List[Connection]
                 )

object Share extends Function3[String, Boolean, List[Connection], Share]
{
    implicit val shareWrites : Writes[Share] = (
            (JsPath \ "message").write[String] and
            (JsPath \ "canEdit").write[Boolean] and
            (JsPath \ "receivers").write[List[Connection]]
    )(unlift(Share.unapply))

    implicit val shareReads : Reads[Share] = (
          (JsPath \ "message").read[String] and
          (JsPath \ "canEdit").read[Boolean] and
          (JsPath \ "receivers").read[List[Connection]]
    )(Share)
}
