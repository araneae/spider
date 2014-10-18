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
case class SharedDocument(
                   userId: Long,
                   documentId: Long,
                   sharedUserId: Long,
                   canCopy: Boolean,
                   canShare: Boolean,
                   canView: Boolean,
                   createdUserId: Long,
                   createdAt: DateTime = new DateTime(),
                   updatedUserId: Option[Long] = None,
                   updatedAt: Option[DateTime] = None
                   )

object SharedDocument extends Function10[Long, Long, Long, Boolean, Boolean, Boolean, Long, DateTime, Option[Long], Option[DateTime], SharedDocument]
{
    implicit val documentWrites : Writes[SharedDocument] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "documentId").write[Long] and
            (JsPath \ "sharedUserId").write[Long] and
            (JsPath \ "canCopy").write[Boolean] and
            (JsPath \ "canShare").write[Boolean] and
            (JsPath \ "canView").write[Boolean] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(SharedDocument.unapply))

    implicit val documentReads : Reads[SharedDocument] = (
          (JsPath \ "userId").read[Long] and
          (JsPath \ "documentId").read[Long] and
          (JsPath \ "sharedUserId").read[Long] and
          (JsPath \ "canCopy").read[Boolean] and
          (JsPath \ "canShare").read[Boolean] and
          (JsPath \ "canView").read[Boolean] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(SharedDocument)
}
