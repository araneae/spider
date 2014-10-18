package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import org.joda.time.DateTime
import enums.OwnershipType._

/**
 * Mapping between user and document created due to sharing a document among users.
 * 
 */
case class UserDocument(
                   userId: Long,
                   documentId: Long,
                   ownershipType: OwnershipType,
                   canCopy: Boolean,
                   canShare: Boolean,
                   canView: Boolean,
                   createdUserId: Long,
                   createdAt: DateTime = new DateTime(),
                   updatedUserId: Option[Long] = None,
                   updatedAt: Option[DateTime] = None
                   )

object UserDocument extends Function10[Long, Long, OwnershipType, Boolean, Boolean, Boolean, Long, DateTime, Option[Long], Option[DateTime], UserDocument]
{
    implicit val documentWrites : Writes[UserDocument] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "documentId").write[Long] and
            (JsPath \ "ownershipType").write[OwnershipType] and
            (JsPath \ "canCopy").write[Boolean] and
            (JsPath \ "canShare").write[Boolean] and
            (JsPath \ "canView").write[Boolean] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(UserDocument.unapply))

    implicit val documentReads : Reads[UserDocument] = (
          (JsPath \ "userId").read[Long] and
          (JsPath \ "documentId").read[Long] and
          (JsPath \ "ownershipType").read[OwnershipType] and
          (JsPath \ "canCopy").read[Boolean] and
          (JsPath \ "canShare").read[Boolean] and
          (JsPath \ "canView").read[Boolean] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(UserDocument)
}
