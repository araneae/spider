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
                          documentId: Long,
                          snippets: Array[String])

object DocumentSearchResult extends Function2[Long, Array[String], DocumentSearchResult]
{
    implicit val documentSearchResultWrites : Writes[DocumentSearchResult] = (
            (JsPath \ "documentId").write[Long] and
            (JsPath \ "snippets").write[Array[String]]
    )(unlift(DocumentSearchResult.unapply))

    implicit val documentSearchResultReads : Reads[DocumentSearchResult] = (
          (JsPath \ "documentId").read[Long] and
          (JsPath \ "snippets").read[Array[String]]
    )(DocumentSearchResult)
}
