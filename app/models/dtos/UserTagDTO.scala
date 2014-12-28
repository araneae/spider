package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import org.joda.time.DateTime

/**
 * User tag DTO 
 * 
 */
case class UserTagDTO(
                   userTagId: Option[Long],
                   userId: Long,
                   name: String) {
  def this(userTag: UserTag) {
      this(userTag.userTagId,
           userTag.userId,
           userTag.name)
  }
}

object UserTagDTO extends Function3[Option[Long], Long, String, UserTagDTO]
{
    implicit val userTagWrites : Writes[UserTagDTO] = (
            (JsPath \ "userTagId").write[Option[Long]] and
            (JsPath \ "userId").write[Long] and
            (JsPath \ "name").write[String]
    )(unlift(UserTagDTO.unapply))

    implicit val userTagReads : Reads[UserTagDTO] = (
          (JsPath \ "userTagId").readNullable[Long] and
          (JsPath \ "userId").read[Long] and
          (JsPath \ "name").read[String]
    )(UserTagDTO)
    
    def apply(userTag: UserTag) = new UserTagDTO(userTag) 
}
