package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

case class User(userId: Option[Long],
                firstName: String,
                lastName: String,
                email: String,
                password: String,
                createdAt: DateTime = new DateTime(),
                updatedAt: Option[DateTime] = None)

// case class User(userId:String,email:String,loginId:String,fullName:String,firstName:String,lastName:String,location:String,homeTown:String,providerId:String,provider:String,state:String,zip:String,accessKey:String,refreshKey:String,avatarUrl:String)

object User extends Function7[Option[Long], String, String, String, String, DateTime, Option[DateTime], User]
{
    implicit val userWrites : Writes[User] = (
            (JsPath \ "userId").write[Option[Long]] and
            (JsPath \ "firstName").write[String] and
            (JsPath \ "lastName").write[String] and
            (JsPath \ "email").write[String] and
            (JsPath \ "password").write[String] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(User.unapply))
      
    implicit val userReads : Reads[User] = (
          (JsPath \ "userId").readNullable[Long] and
          (JsPath \ "firstName").read[String] and
          (JsPath \ "lastName").read[String] and
          (JsPath \ "email").read[String] and
          (JsPath \ "password").read[String] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(User)
}
