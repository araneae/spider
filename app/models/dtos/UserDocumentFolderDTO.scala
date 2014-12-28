package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import org.joda.time.DateTime
import enums.OwnershipType._

/**
 * UserDocumentFolder DTO
 * 
 */
case class UserDocumentFolderDTO(
                   userDocumentFolderId: Option[Long],
                   documentFolderId: Long,
                   userId: Long,
                   ownershipType: OwnershipType,
                   canCopy: Boolean,
                   canShare: Boolean,
                   canView: Boolean,
                   isLimitedShare: Boolean,
                   shareUntilEOD: Option[DateTime]) {
    def this(userDocumentFolder: UserDocumentFolder) {
        this(userDocumentFolder.userDocumentFolderId,
             userDocumentFolder.documentFolderId,
             userDocumentFolder.userId,
             userDocumentFolder.ownershipType,
             userDocumentFolder.canCopy,
             userDocumentFolder.canShare,
             userDocumentFolder.canView,
             userDocumentFolder.isLimitedShare,
             userDocumentFolder.shareUntilEOD
            )
    }
}

object UserDocumentFolderDTO extends Function9[Option[Long], Long, Long, OwnershipType, Boolean, Boolean, Boolean, Boolean, Option[DateTime], UserDocumentFolderDTO]
{
    implicit val userDocumentFolderWrites : Writes[UserDocumentFolderDTO] = (
            (JsPath \ "userDocumentFolderId").write[Option[Long]] and
            (JsPath \ "documentFolderId").write[Long] and
            (JsPath \ "userId").write[Long] and
            (JsPath \ "ownershipType").write[OwnershipType] and
            (JsPath \ "canCopy").write[Boolean] and
            (JsPath \ "canShare").write[Boolean] and
            (JsPath \ "canView").write[Boolean] and
            (JsPath \ "isLimitedShare").write[Boolean] and
            (JsPath \ "shareUntilEOD").write[Option[DateTime]]
    )(unlift(UserDocumentFolderDTO.unapply))

    implicit val userDocumentFolderReads : Reads[UserDocumentFolderDTO] = (
          (JsPath \ "userDocumentFolderId").readNullable[Long] and
          (JsPath \ "documentFolderId").read[Long] and
          (JsPath \ "userId").read[Long] and
          (JsPath \ "ownershipType").read[OwnershipType] and
          (JsPath \ "canCopy").read[Boolean] and
          (JsPath \ "canShare").read[Boolean] and
          (JsPath \ "canView").read[Boolean] and
          (JsPath \ "isLimitedShare").read[Boolean] and
          (JsPath \ "shareUntilEOD").readNullable[DateTime]
    )(UserDocumentFolderDTO)
    
    def apply(userDocumentFolder: UserDocumentFolder) = new UserDocumentFolderDTO(userDocumentFolder)
}
