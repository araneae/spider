package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

case class User(userId: Option[Long],
                firstName: String,
                lastName: String,
                email: String,
                password: String,
                countryId: Long,
                activationToken: String,
                verified: Boolean,
                otp: Option[String] = None,
                otpExpiredAt: Option[DateTime] = None,
                createdAt: DateTime = new DateTime(),
                updatedAt: Option[DateTime] = None)

// case class User(userId:String,email:String,loginId:String,fullName:String,firstName:String,lastName:String,location:String,homeTown:String,providerId:String,provider:String,state:String,zip:String,accessKey:String,refreshKey:String,avatarUrl:String)

object User extends Function12[Option[Long], String, String, String, String, Long, String, Boolean, Option[String], Option[DateTime], DateTime, Option[DateTime], User]
{
    implicit val userWrites : Writes[User] = (
            (JsPath \ "userId").write[Option[Long]] and
            (JsPath \ "firstName").write[String] and
            (JsPath \ "lastName").write[String] and
            (JsPath \ "email").write[String] and
            (JsPath \ "password").write[String] and
            (JsPath \ "countryId").write[Long] and
            (JsPath \ "activationToken").write[String] and
            (JsPath \ "verified").write[Boolean] and
            (JsPath \ "otp").write[Option[String]] and
            (JsPath \ "otpExpiredAt").write[Option[DateTime]] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(User.unapply))
      
    implicit val userReads : Reads[User] = (
          (JsPath \ "userId").readNullable[Long] and
          (JsPath \ "firstName").read[String] and
          (JsPath \ "lastName").read[String] and
          (JsPath \ "email").read[String] and
          (JsPath \ "password").read[String] and
          (JsPath \ "countryId").read[Long] and
          (JsPath \ "activationToken").read[String] and
          (JsPath \ "verified").read[Boolean] and
          (JsPath \ "otp").readNullable[String] and
          (JsPath \ "otpExpiredAt").readNullable[DateTime] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(User)
}
