package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._

/**
 * User can upload multiple resumes.
 * 
 */
case class Document(id: Option[Long],
                   userId: Long,
                   name: String,
                   documentType: DocumentType,
                   fileType: FileType,
                   fileName: String,
                   physicalName: String,
                   description: String)

object Document extends Function8[Option[Long], Long, String, DocumentType, FileType, String, String, String, Document]
{
    implicit val documentWrites : Writes[Document] = (
            (JsPath \ "id").write[Option[Long]] and
            (JsPath \ "userId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "documentType").write[DocumentType] and
            (JsPath \ "fileType").write[FileType] and
            (JsPath \ "fileName").write[String] and
            (JsPath \ "physicalName").write[String] and
            (JsPath \ "description").write[String]
    )(unlift(Document.unapply))

    implicit val documentReads : Reads[Document] = (
          (JsPath \ "id").readNullable[Long] and
          (JsPath \ "userId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "documentType").read[DocumentType] and
          (JsPath \ "fileType").read[FileType] and
          (JsPath \ "fileName").read[String] and
          (JsPath \ "physicalName").read[String] and
          (JsPath \ "description").read[String]
    )(Document)
}
