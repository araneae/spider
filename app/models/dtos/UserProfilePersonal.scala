package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.GenderType._
import enums.MaritalStatusType._
import enums.EducationLevelType._
import enums.WorkStatusType._

case class UserPersonalProfile(
                userProfileId: Option[Long],
                aboutMe: Option[String],
                picture: Option[String],
                mobile: Option[String],
                alternativeEmail: Option[String],
                gender: Option[GenderType],
                maritalStatus: Option[MaritalStatusType],
                birthYear: Option[Int],
                birthMonth: Option[Int],
                birthDay: Option[Int],
                xrayTerms: String,
                createdAt: DateTime = new DateTime(),
                updatedAt: Option[DateTime] = None
                )

object UserPersonalProfile extends Function13[Option[Long], Option[String], Option[String], Option[String], Option[String], Option[GenderType],
                                       Option[MaritalStatusType], Option[Int], Option[Int], Option[Int], String, DateTime, Option[DateTime], UserPersonalProfile]
{
    implicit val userPersonalProfileWrites : Writes[UserPersonalProfile] = (
            (JsPath \ "userPersonalProfileId").write[Option[Long]] and
            (JsPath \ "aboutMe").write[Option[String]] and
            (JsPath \ "picture").write[Option[String]] and
            (JsPath \ "mobile").write[Option[String]] and
            (JsPath \ "alternativeEmail").write[Option[String]] and
            (JsPath \ "gender").write[Option[GenderType]] and
            (JsPath \ "maritalStatus").write[Option[MaritalStatusType]] and
            (JsPath \ "birthYear").write[Option[Int]] and
            (JsPath \ "birthMonth").write[Option[Int]] and
            (JsPath \ "birthDay").write[Option[Int]] and
            (JsPath \ "xrayTerms").write[String] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(UserPersonalProfile.unapply))
      
    implicit val userPersonalProfileReads : Reads[UserPersonalProfile] = (
            (JsPath \ "userPersonalProfileId").readNullable[Long] and
            (JsPath \ "aboutMe").readNullable[String] and
            (JsPath \ "picture").readNullable[String] and
            (JsPath \ "mobile").readNullable[String] and
            (JsPath \ "alternativeEmail").readNullable[String] and
            (JsPath \ "gender").readNullable[GenderType] and
            (JsPath \ "maritalStatus").readNullable[MaritalStatusType] and
            (JsPath \ "birthYear").readNullable[Int] and
            (JsPath \ "birthMonth").readNullable[Int] and
            (JsPath \ "birthDay").readNullable[Int] and
            (JsPath \ "xrayTerms").read[String] and
            (JsPath \ "createdAt").read[DateTime] and
            (JsPath \ "updatedAt").readNullable[DateTime]
    )(UserPersonalProfile)
}
