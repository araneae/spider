package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._

/**
 * All connections - contacts with shared document attributes
 * 
 */
case class ContactWithDocument(
                 id: Long,  // userId
                 text: String, // email
                 documentId: Long,
                 shared: Boolean,
                 canShare: Option[Boolean],
                 canCopy: Option[Boolean]
                 )

object ContactWithDocument extends Function6[Long, String, Long, Boolean, Option[Boolean], Option[Boolean], ContactWithDocument]
{
    implicit val connectionWrites : Writes[ContactWithDocument] = (
            (JsPath \ "id").write[Long] and
            (JsPath \ "text").write[String] and
            (JsPath \ "documentId").write[Long] and
            (JsPath \ "shared").write[Boolean] and
            (JsPath \ "canShare").write[Option[Boolean]] and
            (JsPath \ "canCopy").write[Option[Boolean]]
    )(unlift(ContactWithDocument.unapply))

    implicit val connectionReads : Reads[ContactWithDocument] = (
          (JsPath \ "id").read[Long] and
          (JsPath \ "text").read[String] and
          (JsPath \ "documentId").read[Long] and
          (JsPath \ "shared").read[Boolean] and
          (JsPath \ "canShare").readNullable[Boolean] and
          (JsPath \ "canCopy").readNullable[Boolean]
    )(ContactWithDocument)
}
