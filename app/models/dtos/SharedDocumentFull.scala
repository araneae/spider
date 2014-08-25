package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import org.joda.time.DateTime

/**
 * User can have shared documents.
 * 
 */
case class SharedDocumentFull(
                   documentId: Long,
                   name: String,
                   sharedBy: String,
                   signature: String,
                   canCopy: Boolean,
                   canShare: Boolean,
                   createdAt: DateTime
                   )

object SharedDocumentFull extends Function7[Long, String, String, String, Boolean, Boolean, DateTime, SharedDocumentFull]
{
    implicit val documentWrites : Writes[SharedDocumentFull] = (
            (JsPath \ "documentId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "sharedBy").write[String] and
            (JsPath \ "signature").write[String] and
            (JsPath \ "canCopy").write[Boolean] and
            (JsPath \ "canShare").write[Boolean] and
            (JsPath \ "createdAt").write[DateTime]
    )(unlift(SharedDocumentFull.unapply))

    implicit val documentReads : Reads[SharedDocumentFull] = (
          (JsPath \ "documentId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "sharedBy").read[String] and
          (JsPath \ "signature").read[String] and
          (JsPath \ "canCopy").read[Boolean] and
          (JsPath \ "canShare").read[Boolean] and
          (JsPath \ "createdAt").read[DateTime]
    )(SharedDocumentFull)
}
