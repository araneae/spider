package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.UserStatusType._

case class User(userId: Option[Long],
                firstName: String,
                middleName: Option[String],
                lastName: String,
                email: String,
                secondEmail: Option[String],
                password: String,
                countryId: Long,
                activationToken: String,
                verified: Boolean,
                lastLogon: DateTime,
                status: UserStatusType,
                userProfilePersonalId: Option[Long],
                otp: Option[String] = None,
                otpExpiredAt: Option[DateTime] = None,
                createdAt: DateTime = new DateTime(),
                updatedAt: Option[DateTime] = None)

object User extends Function17[Option[Long], String, Option[String], String, String, Option[String], String, Long, String, Boolean, DateTime, UserStatusType, Option[Long], Option[String], Option[DateTime], DateTime, Option[DateTime], User]
{
    implicit val userWrites : Writes[User] = (
            (JsPath \ "userId").write[Option[Long]] and
            (JsPath \ "firstName").write[String] and
            (JsPath \ "middleName").write[Option[String]] and
            (JsPath \ "lastName").write[String] and
            (JsPath \ "email").write[String] and
            (JsPath \ "secondEmail").write[Option[String]] and
            (JsPath \ "password").write[String] and
            (JsPath \ "countryId").write[Long] and
            (JsPath \ "activationToken").write[String] and
            (JsPath \ "verified").write[Boolean] and
            (JsPath \ "lastLogon").write[DateTime] and
            (JsPath \ "status").write[UserStatusType] and
            (JsPath \ "userProfilePersonalId").write[Option[Long]] and
            (JsPath \ "otp").write[Option[String]] and
            (JsPath \ "otpExpiredAt").write[Option[DateTime]] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(User.unapply))
      
    implicit val userReads : Reads[User] = (
          (JsPath \ "userId").readNullable[Long] and
          (JsPath \ "firstName").read[String] and
          (JsPath \ "middleName").readNullable[String] and
          (JsPath \ "lastName").read[String] and
          (JsPath \ "email").read[String] and
          (JsPath \ "secondEmail").readNullable[String] and
          (JsPath \ "password").read[String] and
          (JsPath \ "countryId").read[Long] and
          (JsPath \ "activationToken").read[String] and
          (JsPath \ "verified").read[Boolean] and
          (JsPath \ "lastLogon").read[DateTime] and
          (JsPath \ "status").read[UserStatusType] and
          (JsPath \ "userProfilePersonalId").readNullable[Long] and
          (JsPath \ "otp").readNullable[String] and
          (JsPath \ "otpExpiredAt").readNullable[DateTime] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(User)
}
