package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._

/**
 * User can attach multiple tags - to organize 
 *    1) documents
 *    2) contacts
 * 
 */
case class UserTag(id: Option[Long],
                   userId: Long,
                   name: String)

object UserTag extends Function3[Option[Long], Long, String, UserTag]
{
    implicit val userTagWrites : Writes[UserTag] = (
            (JsPath \ "id").write[Option[Long]] and
            (JsPath \ "userId").write[Long] and
            (JsPath \ "name").write[String]
    )(unlift(UserTag.unapply))

    implicit val userTagReads : Reads[UserTag] = (
          (JsPath \ "id").readNullable[Long] and
          (JsPath \ "userId").read[Long] and
          (JsPath \ "name").read[String]
    )(UserTag)
}
