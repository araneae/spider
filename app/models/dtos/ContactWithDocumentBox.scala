package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import org.joda.time.DateTime

/**
 * All connections - contacts with shared document box attributes
 * 
 */
case class ContactWithDocumentBox(
                 id: Long,  // userId
                 text: String, // email
                 shared: Boolean,
                 canCopy: Option[Boolean],
                 isLimitedShare: Option[Boolean],
                 shareUntilEOD: Option[DateTime]
                 )

object ContactWithDocumentBox extends Function6[Long, String, Boolean, Option[Boolean], Option[Boolean], Option[DateTime], ContactWithDocumentBox]
{
    implicit val connectionWrites : Writes[ContactWithDocumentBox] = (
            (JsPath \ "id").write[Long] and
            (JsPath \ "text").write[String] and
            (JsPath \ "shared").write[Boolean] and
            (JsPath \ "canCopy").write[Option[Boolean]] and
            (JsPath \ "isLimitedShare").write[Option[Boolean]] and
            (JsPath \ "shareUntilEOD").write[Option[DateTime]]
    )(unlift(ContactWithDocumentBox.unapply))

    implicit val connectionReads : Reads[ContactWithDocumentBox] = (
          (JsPath \ "id").read[Long] and
          (JsPath \ "text").read[String] and
          (JsPath \ "shared").read[Boolean] and
          (JsPath \ "canCopy").readNullable[Boolean] and
          (JsPath \ "isLimitedShare").readNullable[Boolean] and
          (JsPath \ "shareUntilEOD").readNullable[DateTime]
    )(ContactWithDocumentBox)
}
