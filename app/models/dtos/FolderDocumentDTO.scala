package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import org.joda.time.DateTime
import enums.OwnershipType._

/**
 * This DTO is for the documents with folder relationship
 * 
 */
case class FolderDocumentDTO(
                   documentFolderId: Long,
                   documentId: Long,
                   name: String,
                   description: String,
                   ownershipType: OwnershipType,
                   signature: String,
                   canCopy: Boolean,
                   canShare: Boolean,
                   canView: Boolean,
                   createdBy: String,
                   createdAt: DateTime
                   ) 

object FolderDocumentDTO extends Function11[Long, Long, String, String, OwnershipType, String, Boolean, Boolean, Boolean, String, DateTime, FolderDocumentDTO]
{
    implicit val documentWrites : Writes[FolderDocumentDTO] = (
            (JsPath \ "documentFolderId").write[Long] and
            (JsPath \ "documentId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "description").write[String] and
            (JsPath \ "ownershipType").write[OwnershipType] and
            (JsPath \ "signature").write[String] and
            (JsPath \ "canCopy").write[Boolean] and
            (JsPath \ "canShare").write[Boolean] and
            (JsPath \ "canView").write[Boolean] and
            (JsPath \ "createdBy").write[String] and
            (JsPath \ "createdAt").write[DateTime]
    )(unlift(FolderDocumentDTO.unapply))

    implicit val documentReads : Reads[FolderDocumentDTO] = (
          (JsPath \ "documentFolderId").read[Long] and
          (JsPath \ "documentId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "description").read[String] and
          (JsPath \ "ownershipType").read[OwnershipType] and
          (JsPath \ "signature").read[String] and
          (JsPath \ "canCopy").read[Boolean] and
          (JsPath \ "canShare").read[Boolean] and
          (JsPath \ "canView").read[Boolean] and
          (JsPath \ "createdBy").read[String] and
          (JsPath \ "createdAt").read[DateTime]
    )(FolderDocumentDTO)
}
