package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._

/**
 * User's document search results.
 * 
 */
case class DocumentSearchResult(
                          userId: Long,
                          documentId: Long,
                          snippets: Array[String])

object DocumentSearchResult extends Function3[Long, Long, Array[String], DocumentSearchResult]
{
    implicit val documentSearchResultWrites : Writes[DocumentSearchResult] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "documentId").write[Long] and
            (JsPath \ "snippets").write[Array[String]]
    )(unlift(DocumentSearchResult.unapply))

    implicit val documentSearchResultReads : Reads[DocumentSearchResult] = (
          (JsPath \ "userId").read[Long] and
          (JsPath \ "documentId").read[Long] and
          (JsPath \ "snippets").read[Array[String]]
    )(DocumentSearchResult)
}
