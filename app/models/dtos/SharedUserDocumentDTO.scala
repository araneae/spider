package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import org.joda.time.DateTime
import enums.OwnershipType._

/**
 * Shared User document DTO
 * 
 */
case class SharedUserDocumentDTO(
                   userDocumentBoxId: Long,
                   documentId: Long,
                   name: String,
                   description: String,
                   canCopy: Boolean,
                   sharedBy: String
                   )

object SharedUserDocumentDTO extends Function6[Long, Long, String, String, Boolean, String, SharedUserDocumentDTO]
{
    implicit val sharedDocumentWrites : Writes[SharedUserDocumentDTO] = (
            (JsPath \ "userDocumentBoxId").write[Long] and
            (JsPath \ "documentId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "description").write[String] and
            (JsPath \ "canCopy").write[Boolean] and
            (JsPath \ "sharedBy").write[String]
    )(unlift(SharedUserDocumentDTO.unapply))

    implicit val sharedDocumentReads : Reads[SharedUserDocumentDTO] = (
          (JsPath \ "userDocumentBoxId").read[Long] and
          (JsPath \ "documentId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "description").read[String] and
          (JsPath \ "canCopy").read[Boolean] and
          (JsPath \ "sharedBy").read[String]
    )(SharedUserDocumentDTO)
}
