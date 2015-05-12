package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.OwnershipType

/**
 * DocumentFolder.
 * 
 */
case class FolderDTO(
                   documentFolderId: Option[Long],
                   name: String,
                   shared: Boolean,
                   shareCount: Long,
                   canCopy: Boolean,
                   canShare: Boolean,
                   canView: Boolean,
                   default: Boolean) {
  def this(userDocumentFolder: UserDocumentFolder, documentFolder: DocumentFolder) {
      this(documentFolder.documentFolderId,
           documentFolder.name,
           userDocumentFolder.ownershipType == OwnershipType.SHARED,
           0,
           userDocumentFolder.canCopy,
           userDocumentFolder.canShare,
           userDocumentFolder.canView,
           documentFolder.default)
  }
}

object FolderDTO extends Function8[Option[Long], String, Boolean, Long, Boolean, Boolean, Boolean, Boolean, FolderDTO]
{
    implicit val documentFolderWrites : Writes[FolderDTO] = (
            (JsPath \ "documentFolderId").write[Option[Long]] and
            (JsPath \ "name").write[String] and
            (JsPath \ "shared").write[Boolean] and
            (JsPath \ "shareCount").write[Long] and
            (JsPath \ "canCopy").write[Boolean] and
            (JsPath \ "canShare").write[Boolean] and
            (JsPath \ "canView").write[Boolean] and
            (JsPath \ "default").write[Boolean]
    )(unlift(FolderDTO.unapply))

    implicit val documentFolderReads : Reads[FolderDTO] = (
          (JsPath \ "documentFolderId").readNullable[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "shared").read[Boolean] and
          (JsPath \ "shareCount").read[Long] and
          (JsPath \ "canCopy").read[Boolean] and
            (JsPath \ "canShare").read[Boolean] and
            (JsPath \ "canView").read[Boolean] and
          (JsPath \ "default").read[Boolean]
    )(FolderDTO)
    
    def apply(userDocumentFolder: UserDocumentFolder, documentFolder: DocumentFolder) = new FolderDTO(userDocumentFolder, documentFolder)
}
