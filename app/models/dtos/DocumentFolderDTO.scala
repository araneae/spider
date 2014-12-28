package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * DocumentFolder.
 * 
 */
case class DocumentFolderDTO(
                   documentFolderId: Option[Long],
                   name: String,
                   default: Boolean) {
  def this(documentFolder: DocumentFolder) {
      this(documentFolder.documentFolderId,
           documentFolder.name,
           documentFolder.default)
  }
}

object DocumentFolderDTO extends Function3[Option[Long], String, Boolean, DocumentFolderDTO]
{
    implicit val documentFolderWrites : Writes[DocumentFolderDTO] = (
            (JsPath \ "documentFolderId").write[Option[Long]] and
            (JsPath \ "name").write[String] and
            (JsPath \ "default").write[Boolean]
    )(unlift(DocumentFolderDTO.unapply))

    implicit val documentFolderReads : Reads[DocumentFolderDTO] = (
          (JsPath \ "documentFolderId").readNullable[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "default").read[Boolean]
    )(DocumentFolderDTO)
    
    def apply(documentFolder: DocumentFolder) = new DocumentFolderDTO(documentFolder)
}
