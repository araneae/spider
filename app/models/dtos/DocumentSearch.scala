package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._

/**
 * User's document saved search texts.
 * 
 */
case class DocumentSearch(documentSearchId: Option[Long],
                          userId: Long,
                          name: String,
                          searchText: String)

object DocumentSearch extends Function4[Option[Long], Long, String, String, DocumentSearch]
{
    implicit val documentSearchWrites : Writes[DocumentSearch] = (
            (JsPath \ "documentSearchId").write[Option[Long]] and
            (JsPath \ "userId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "searchText").write[String]
    )(unlift(DocumentSearch.unapply))

    implicit val documentSearchReads : Reads[DocumentSearch] = (
          (JsPath \ "documentSearchId").readNullable[Long] and
          (JsPath \ "userId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "searchText").read[String]
    )(DocumentSearch)
}
