package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import org.joda.time.DateTime

/**
 * User can upload multiple resumes.
 * 
 */
case class Document(documentId: Option[Long],
                   userId: Long,
                   name: String,
                   documentType: DocumentType,
                   fileType: FileType,
                   fileName: String,
                   physicalName: String,
                   description: Option[String],
                   signature: String,
                   createdUserId: Long,
                   createdAt: DateTime = new DateTime(),
                   updatedUserId: Option[Long] = None,
                   updatedAt: Option[DateTime] = None)

object Document extends Function13[Option[Long], Long, String, DocumentType, FileType, String, String, Option[String], String, Long, DateTime, Option[Long], Option[DateTime], Document]
{
    implicit val documentWrites : Writes[Document] = (
            (JsPath \ "documentId").write[Option[Long]] and
            (JsPath \ "userId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "documentType").write[DocumentType] and
            (JsPath \ "fileType").write[FileType] and
            (JsPath \ "fileName").write[String] and
            (JsPath \ "physicalName").write[String] and
            (JsPath \ "description").write[Option[String]] and
            (JsPath \ "signature").write[String] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(Document.unapply))

    implicit val documentReads : Reads[Document] = (
          (JsPath \ "documentId").readNullable[Long] and
          (JsPath \ "userId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "documentType").read[DocumentType] and
          (JsPath \ "fileType").read[FileType] and
          (JsPath \ "fileName").read[String] and
          (JsPath \ "physicalName").read[String] and
          (JsPath \ "description").readNullable[String] and
          (JsPath \ "signature").read[String] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(Document)
}
