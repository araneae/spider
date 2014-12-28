package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import org.joda.time.DateTime

/**
 * All connections - contacts with shared document attributes
 * 
 */
case class ContactWithDocument(
                 id: Long,  // userId
                 text: String, // name
                 documentId: Option[Long],
                 canShare: Option[Boolean],
                 canCopy: Option[Boolean],
                 canView: Option[Boolean],
                 isLimitedShare: Option[Boolean],
                 shareUntilEOD: Option[DateTime]
                 )

object ContactWithDocument extends Function8[Long, String, Option[Long], Option[Boolean], Option[Boolean],  Option[Boolean], Option[Boolean], Option[DateTime], ContactWithDocument]
{
    implicit val connectionWrites : Writes[ContactWithDocument] = (
            (JsPath \ "id").write[Long] and
            (JsPath \ "text").write[String] and
            (JsPath \ "documentId").write[Option[Long]] and
            (JsPath \ "canShare").write[Option[Boolean]] and
            (JsPath \ "canCopy").write[Option[Boolean]] and
            (JsPath \ "canView").write[Option[Boolean]] and
            (JsPath \ "isLimitedShare").write[Option[Boolean]] and
            (JsPath \ "shareUntilEOD").write[Option[DateTime]]
    )(unlift(ContactWithDocument.unapply))

    implicit val connectionReads : Reads[ContactWithDocument] = (
          (JsPath \ "id").read[Long] and
          (JsPath \ "text").read[String] and
          (JsPath \ "documentId").readNullable[Long] and
          (JsPath \ "canShare").readNullable[Boolean] and
          (JsPath \ "canCopy").readNullable[Boolean] and
          (JsPath \ "canView").readNullable[Boolean] and
          (JsPath \ "isLimitedShare").readNullable[Boolean] and
          (JsPath \ "shareUntilEOD").readNullable[DateTime]
    )(ContactWithDocument)
}
