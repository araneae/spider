package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.OwnershipType._
import enums.ContactStatus._
import enums.FileType._
import org.joda.time.DateTime

/**
 * All connections - contacts with shared document box attributes
 * 
 */
case class ContactWithDocumentFolder(
                 id: Long,  // userId
                 text: String, // email
                 shared: Boolean,
                 ownershipType: Option[OwnershipType],
                 canCopy: Option[Boolean],
                 canShare: Option[Boolean],
                 canView: Option[Boolean],
                 isLimitedShare: Option[Boolean],
                 shareUntilEOD: Option[DateTime]
                 )

object ContactWithDocumentFolder extends Function9[Long, String, Boolean, Option[OwnershipType], Option[Boolean], Option[Boolean], Option[Boolean], Option[Boolean], Option[DateTime], ContactWithDocumentFolder]
{
    implicit val connectionWrites : Writes[ContactWithDocumentFolder] = (
            (JsPath \ "id").write[Long] and
            (JsPath \ "text").write[String] and
            (JsPath \ "shared").write[Boolean] and
            (JsPath \ "ownershipType").write[Option[OwnershipType]] and
            (JsPath \ "canCopy").write[Option[Boolean]] and
            (JsPath \ "canShare").write[Option[Boolean]] and
            (JsPath \ "canView").write[Option[Boolean]] and
            (JsPath \ "isLimitedShare").write[Option[Boolean]] and
            (JsPath \ "shareUntilEOD").write[Option[DateTime]]
    )(unlift(ContactWithDocumentFolder.unapply))

    implicit val connectionReads : Reads[ContactWithDocumentFolder] = (
          (JsPath \ "id").read[Long] and
          (JsPath \ "text").read[String] and
          (JsPath \ "shared").read[Boolean] and
          (JsPath \ "ownershipType").readNullable[OwnershipType] and
          (JsPath \ "canCopy").readNullable[Boolean] and
          (JsPath \ "canShare").readNullable[Boolean] and
          (JsPath \ "canView").readNullable[Boolean] and
          (JsPath \ "isLimitedShare").readNullable[Boolean] and
          (JsPath \ "shareUntilEOD").readNullable[DateTime]
    )(ContactWithDocumentFolder)
}
