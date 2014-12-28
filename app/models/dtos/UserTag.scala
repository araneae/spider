package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import org.joda.time.DateTime

/**
 * User can attach multiple tags - to organize 
 *    1) documents
 *    2) contacts
 * 
 */
case class UserTag(userTagId: Option[Long],
                   userId: Long,
                   name: String,
                   createdUserId: Long,
                   createdAt: DateTime = new DateTime(),
                   updatedUserId: Option[Long] = None,
                   updatedAt: Option[DateTime] = None) {
  def this(userTagDTO: UserTagDTO,
           createdUserId: Long,
           createdAt: DateTime,
           updatedUserId: Option[Long],
           updatedAt: Option[DateTime]) {
      this(userTagDTO.userTagId,
           userTagDTO.userId,
           userTagDTO.name,
           createdUserId,
           createdAt,
           updatedUserId,
           updatedAt)
  }
}

object UserTag extends Function7[Option[Long], Long, String, Long, DateTime, Option[Long], Option[DateTime], UserTag]
{
    implicit val userTagWrites : Writes[UserTag] = (
            (JsPath \ "userTagId").write[Option[Long]] and
            (JsPath \ "userId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(UserTag.unapply))

    implicit val userTagReads : Reads[UserTag] = (
          (JsPath \ "userTagId").readNullable[Long] and
          (JsPath \ "userId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(UserTag)
    
    def apply(userTagDTO: UserTagDTO,
           createdUserId: Long,
           createdAt: DateTime,
           updatedUserId: Option[Long],
           updatedAt: Option[DateTime]) = new UserTag(userTagDTO, createdUserId, createdAt, updatedUserId, updatedAt)
}
