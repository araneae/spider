package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import org.joda.time.DateTime

/**
 * User can attach multiple tags to each document.
 * 
 */
case class DocumentTag(userId: Long,
                       userTagId: Long,
                       documentId: Long,
                       userDocumentId: Long,
                       createdUserId: Long,
                       createdAt: DateTime = new DateTime(),
                       updatedUserId: Option[Long] = None,
                       updatedAt: Option[DateTime] = None)

object DocumentTag extends Function8[Long, Long, Long, Long, Long, DateTime, Option[Long], Option[DateTime], DocumentTag]
{
    implicit val documentTagWrites : Writes[DocumentTag] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "userTagId").write[Long] and
            (JsPath \ "documentId").write[Long] and
            (JsPath \ "userDocumentId").write[Long] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(DocumentTag.unapply))

    implicit val documentTagReads : Reads[DocumentTag] = (
          (JsPath \ "userId").read[Long] and
          (JsPath \ "userTagId").read[Long] and
          (JsPath \ "documentId").read[Long] and
          (JsPath \ "userDocumentId").read[Long] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(DocumentTag)
}
