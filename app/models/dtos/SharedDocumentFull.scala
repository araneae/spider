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
                   canCopy: Boolean
                   )

object SharedDocumentFull extends Function4[Long, String, String, Boolean, SharedDocumentFull]
{
    implicit val documentWrites : Writes[SharedDocumentFull] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "sharedBy").write[String] and
            (JsPath \ "canCopy").write[Boolean]
    )(unlift(SharedDocumentFull.unapply))

    implicit val documentReads : Reads[SharedDocumentFull] = (
          (JsPath \ "userId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "sharedBy").read[String] and
          (JsPath \ "canCopy").read[Boolean]
    )(SharedDocumentFull)
}
