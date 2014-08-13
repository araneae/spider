package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import org.joda.time.DateTime
import org.joda.time.DateTime

/**
 * User's document saved search texts.
 * 
 */
case class DocumentSearch(documentSearchId: Option[Long],
                          userId: Long,
                          name: String,
                          searchText: String,
                          createdUserId: Long,
                          createdAt: DateTime = new DateTime(),
                          updatedUserId: Option[Long] = None,
                          updatedAt: Option[DateTime] = None)

object DocumentSearch extends Function8[Option[Long], Long, String, String, Long, DateTime, Option[Long], Option[DateTime], DocumentSearch]
{
    implicit val documentSearchWrites : Writes[DocumentSearch] = (
            (JsPath \ "documentSearchId").write[Option[Long]] and
            (JsPath \ "userId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "searchText").write[String] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(DocumentSearch.unapply))

    implicit val documentSearchReads : Reads[DocumentSearch] = (
          (JsPath \ "documentSearchId").readNullable[Long] and
          (JsPath \ "userId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "searchText").read[String] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(DocumentSearch)
}
