package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import org.joda.time.DateTime
import enums.OwnershipType._

/**
 * Mapping between users and document store.
 * 
 */
case class UserDocumentBox(
                   userDocumentBoxId: Option[Long],
                   documentBoxId: Long,
                   userId: Long,
                   ownershipType: OwnershipType,
                   canCopy: Boolean,
                   isLimitedShare: Boolean,
                   shareUntilEOD: Option[DateTime],
                   createdUserId: Long,
                   createdAt: DateTime = new DateTime(),
                   updatedUserId: Option[Long] = None,
                   updatedAt: Option[DateTime] = None
                   )

object UserDocumentBox extends Function11[Option[Long], Long, Long, OwnershipType, Boolean, Boolean, Option[DateTime], Long, DateTime, Option[Long], Option[DateTime], UserDocumentBox]
{
    implicit val userDocumentBoxWrites : Writes[UserDocumentBox] = (
            (JsPath \ "userDocumentBoxId").write[Option[Long]] and
            (JsPath \ "documentBoxId").write[Long] and
            (JsPath \ "userId").write[Long] and
            (JsPath \ "ownershipType").write[OwnershipType] and
            (JsPath \ "canCopy").write[Boolean] and
            (JsPath \ "isLimitedShare").write[Boolean] and
            (JsPath \ "shareUntilEOD").write[Option[DateTime]] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(UserDocumentBox.unapply))

    implicit val userDocumentBoxReads : Reads[UserDocumentBox] = (
          (JsPath \ "userDocumentBoxId").readNullable[Long] and
          (JsPath \ "documentBoxId").read[Long] and
          (JsPath \ "userId").read[Long] and
          (JsPath \ "ownershipType").read[OwnershipType] and
          (JsPath \ "canCopy").read[Boolean] and
          (JsPath \ "isLimitedShare").read[Boolean] and
          (JsPath \ "shareUntilEOD").readNullable[DateTime] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(UserDocumentBox)
}
