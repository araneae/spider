package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * DocumentFolder.
 * 
 */
case class DocumentFolder(
                   documentFolderId: Option[Long],
                   name: String,
                   default: Boolean,
                   createdUserId: Long,
                   createdAt: DateTime = new DateTime(),
                   updatedUserId: Option[Long] = None,
                   updatedAt: Option[DateTime] = None) {
  def this(documentFolderDTO: DocumentFolderDTO,
          createdUserId: Long,
          createdAt: DateTime,
          updatedUserId: Option[Long],
          updatedAt: Option[DateTime]) {
      this(documentFolderDTO.documentFolderId,
           documentFolderDTO.name,
           documentFolderDTO.default,
           createdUserId,
           createdAt,
           updatedUserId,
           updatedAt)
  }
  
  def this(folderDTO: FolderDTO,
          createdUserId: Long,
          createdAt: DateTime,
          updatedUserId: Option[Long],
          updatedAt: Option[DateTime]) {
      this(folderDTO.documentFolderId,
           folderDTO.name,
           folderDTO.default,
           createdUserId,
           createdAt,
           updatedUserId,
           updatedAt)
  }
}

object DocumentFolder extends Function7[Option[Long], String, Boolean, Long, DateTime, Option[Long], Option[DateTime], DocumentFolder]
{
    implicit val documentFolderWrites : Writes[DocumentFolder] = (
            (JsPath \ "documentFolderId").write[Option[Long]] and
            (JsPath \ "name").write[String] and
            (JsPath \ "default").write[Boolean] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(DocumentFolder.unapply))

    implicit val documentFolderReads : Reads[DocumentFolder] = (
          (JsPath \ "documentFolderId").readNullable[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "default").read[Boolean] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(DocumentFolder)
    
    def apply(documentFolderDTO: DocumentFolderDTO,
          createdUserId: Long,
          createdAt: DateTime,
          updatedUserId: Option[Long],
          updatedAt: Option[DateTime]) = new DocumentFolder(documentFolderDTO, createdUserId, createdAt, updatedUserId, updatedAt)
    
    def apply(folderDTO: FolderDTO,
          createdUserId: Long,
          createdAt: DateTime,
          updatedUserId: Option[Long],
          updatedAt: Option[DateTime]) = new DocumentFolder(folderDTO, createdUserId, createdAt, updatedUserId, updatedAt)
}
