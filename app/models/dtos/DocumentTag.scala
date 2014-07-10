package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._

/**
 * User can attach multiple tags to each document.
 * 
 */
case class DocumentTag(userId: Long,
                       userTagId: Long,
                       documentId: Long)

object DocumentTag extends Function3[Long, Long, Long, DocumentTag]
{
    implicit val documentTagWrites : Writes[DocumentTag] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "userTagId").write[Long] and
            (JsPath \ "documentId").write[Long]
    )(unlift(DocumentTag.unapply))

    implicit val documentTagReads : Reads[DocumentTag] = (
          (JsPath \ "userId").read[Long] and
          (JsPath \ "userTagId").read[Long] and
          (JsPath \ "documentId").read[Long]
    )(DocumentTag)
}
