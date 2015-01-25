package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import org.joda.time.DateTime
import enums.SearchQueryType._

/**
 * User's saved search queries
 * 
 */
case class SearchQuery(searchQueryId: Option[Long],
                       userId: Long,
                       queryType: SearchQueryType,
                       name: String,
                       query: String,
                       createdUserId: Long,
                       createdAt: DateTime = new DateTime(),
                       updatedUserId: Option[Long] = None,
                       updatedAt: Option[DateTime] = None)

object SearchQuery extends Function9[Option[Long], Long, SearchQueryType, String, String, Long, DateTime, Option[Long], Option[DateTime], SearchQuery]
{
    implicit val searchQueryWrites : Writes[SearchQuery] = (
            (JsPath \ "searchQueryId").write[Option[Long]] and
            (JsPath \ "userId").write[Long] and
            (JsPath \ "queryType").write[SearchQueryType] and
            (JsPath \ "name").write[String] and
            (JsPath \ "query").write[String] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(SearchQuery.unapply))

    implicit val searchQueryReads : Reads[SearchQuery] = (
          (JsPath \ "searchQueryId").readNullable[Long] and
          (JsPath \ "userId").read[Long] and
          (JsPath \ "queryType").read[SearchQueryType] and
          (JsPath \ "name").read[String] and
          (JsPath \ "query").read[String] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(SearchQuery)
}
