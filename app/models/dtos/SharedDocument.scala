package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._

/**
 * User can have shared documents.
 * 
 */
case class SharedDocument(
                   userId: Long,
                   documentId: Long,
                   sharedUserId: Long,
                   canCopy: Boolean
                   )

object SharedDocument extends Function4[Long, Long, Long, Boolean, SharedDocument]
{
    implicit val documentWrites : Writes[SharedDocument] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "documentId").write[Long] and
            (JsPath \ "sharedUserId").write[Long] and
            (JsPath \ "canCopy").write[Boolean]
    )(unlift(SharedDocument.unapply))

    implicit val documentReads : Reads[SharedDocument] = (
          (JsPath \ "userId").read[Long] and
          (JsPath \ "documentId").read[Long] and
          (JsPath \ "sharedUserId").read[Long] and
          (JsPath \ "canCopy").read[Boolean]
    )(SharedDocument)
}
