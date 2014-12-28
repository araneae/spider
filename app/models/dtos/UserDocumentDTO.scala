package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import org.joda.time.DateTime
import enums.OwnershipType._

/**
 * User document
 * 
 */
case class UserDocumentDTO(
                   userDocumentId: Option[Long],
                   documentId: Long,
                   name: String,
                   description: String,
                   connected: Boolean,
                   ownershipType: OwnershipType,
                   signature: String,
                   canCopy: Boolean,
                   canShare: Boolean,
                   canView: Boolean,
                   createdBy: String,
                   createdAt: DateTime
                   ) 

object UserDocumentDTO extends Function12[Option[Long], Long, String, String, Boolean, OwnershipType, String, Boolean, Boolean, Boolean, String, DateTime, UserDocumentDTO]
{
    implicit val documentWrites : Writes[UserDocumentDTO] = (
            (JsPath \ "userDocumentId").write[Option[Long]] and
            (JsPath \ "documentId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "description").write[String] and
            (JsPath \ "connected").write[Boolean] and
            (JsPath \ "ownershipType").write[OwnershipType] and
            (JsPath \ "signature").write[String] and
            (JsPath \ "canCopy").write[Boolean] and
            (JsPath \ "canShare").write[Boolean] and
            (JsPath \ "canView").write[Boolean] and
            (JsPath \ "createdBy").write[String] and
            (JsPath \ "createdAt").write[DateTime]
    )(unlift(UserDocumentDTO.unapply))

    implicit val documentReads : Reads[UserDocumentDTO] = (
          (JsPath \ "userDocumentId").readNullable[Long] and
          (JsPath \ "documentId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "description").read[String] and
          (JsPath \ "connected").read[Boolean] and
          (JsPath \ "ownershipType").read[OwnershipType] and
          (JsPath \ "signature").read[String] and
          (JsPath \ "canCopy").read[Boolean] and
          (JsPath \ "canShare").read[Boolean] and
          (JsPath \ "canView").read[Boolean] and
          (JsPath \ "createdBy").read[String] and
          (JsPath \ "createdAt").read[DateTime]
    )(UserDocumentDTO)
}
