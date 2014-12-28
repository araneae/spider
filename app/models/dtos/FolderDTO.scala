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
                   default: Boolean) {
  def this(userDocumentFolder: UserDocumentFolder, documentFolder: DocumentFolder) {
      this(documentFolder.documentFolderId,
           documentFolder.name,
           userDocumentFolder.ownershipType == OwnershipType.SHARED,
           documentFolder.default)
  }
}

object FolderDTO extends Function4[Option[Long], String, Boolean, Boolean, FolderDTO]
{
    implicit val documentFolderWrites : Writes[FolderDTO] = (
            (JsPath \ "documentFolderId").write[Option[Long]] and
            (JsPath \ "name").write[String] and
            (JsPath \ "shared").write[Boolean] and
            (JsPath \ "default").write[Boolean]
    )(unlift(FolderDTO.unapply))

    implicit val documentFolderReads : Reads[FolderDTO] = (
          (JsPath \ "documentFolderId").readNullable[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "shared").read[Boolean] and
          (JsPath \ "default").read[Boolean]
    )(FolderDTO)
    
    def apply(userDocumentFolder: UserDocumentFolder, documentFolder: DocumentFolder) = new FolderDTO(userDocumentFolder, documentFolder)
}
