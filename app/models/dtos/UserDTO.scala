package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

case class UserDTO(userId: Option[Long],
                firstName: String,
                middleName: Option[String],
                lastName: String,
                email: String,
                password: String,
                countryId: Long)

object UserDTO extends Function7[Option[Long], String, Option[String], String, String, String, Long, UserDTO]
{
    implicit val userWrites : Writes[UserDTO] = (
            (JsPath \ "userId").write[Option[Long]] and
            (JsPath \ "firstName").write[String] and
            (JsPath \ "middleName").write[Option[String]] and
            (JsPath \ "lastName").write[String] and
            (JsPath \ "email").write[String] and
            (JsPath \ "password").write[String] and
            (JsPath \ "countryId").write[Long]
    )(unlift(UserDTO.unapply))
      
    implicit val userReads : Reads[UserDTO] = (
          (JsPath \ "userId").readNullable[Long] and
          (JsPath \ "firstName").read[String] and
          (JsPath \ "middleName").readNullable[String] and
          (JsPath \ "lastName").read[String] and
          (JsPath \ "email").read[String] and
          (JsPath \ "password").read[String] and
          (JsPath \ "countryId").read[Long]
    )(UserDTO)
}
