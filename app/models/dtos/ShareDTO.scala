package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * Used by document share POST action
 * 
 */
case class ShareDTO(
                 subject: String,
                 message: String,
                 canCopy: Boolean,
                 canShare: Boolean,
                 canView: Boolean,
                 isLimitedShare: Boolean,
                 shareUntilEOD: Option[DateTime],
                 receivers: List[ConnectionDTO]
                 )

object ShareDTO extends Function8[String, String, Boolean, Boolean, Boolean, Boolean, Option[DateTime], List[ConnectionDTO], ShareDTO]
{
    implicit val shareWrites : Writes[ShareDTO] = (
            (JsPath \ "subject").write[String] and
            (JsPath \ "message").write[String] and
            (JsPath \ "canCopy").write[Boolean] and
            (JsPath \ "canShare").write[Boolean] and
            (JsPath \ "canView").write[Boolean] and
            (JsPath \ "isLimitedShare").write[Boolean] and
            (JsPath \ "shareUntilEOD").write[Option[DateTime]] and
            (JsPath \ "receivers").write[List[ConnectionDTO]]
    )(unlift(ShareDTO.unapply))

    implicit val shareReads : Reads[ShareDTO] = (
          (JsPath \ "subject").read[String] and
          (JsPath \ "message").read[String] and
          (JsPath \ "canCopy").read[Boolean] and
          (JsPath \ "canShare").read[Boolean] and
          (JsPath \ "canView").read[Boolean] and
          (JsPath \ "isLimitedShare").read[Boolean] and
          (JsPath \ "shareUntilEOD").readNullable[DateTime] and
          (JsPath \ "receivers").read[List[ConnectionDTO]]
    )(ShareDTO)
}
