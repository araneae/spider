package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._

/**
 * User can have shared documents.
 * 
 */
case class SharedDocumentFull(
                   documentId: Long,
                   name: String,
                   sharedBy: String,
                   canCopy: Boolean,
                   canShare: Boolean
                   )

object SharedDocumentFull extends Function5[Long, String, String, Boolean, Boolean, SharedDocumentFull]
{
    implicit val documentWrites : Writes[SharedDocumentFull] = (
            (JsPath \ "documentId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "sharedBy").write[String] and
            (JsPath \ "canCopy").write[Boolean] and
            (JsPath \ "canShare").write[Boolean]
    )(unlift(SharedDocumentFull.unapply))

    implicit val documentReads : Reads[SharedDocumentFull] = (
          (JsPath \ "documentId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "sharedBy").read[String] and
          (JsPath \ "canCopy").read[Boolean] and
          (JsPath \ "canShare").read[Boolean]
    )(SharedDocumentFull)
}
