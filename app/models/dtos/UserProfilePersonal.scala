package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.GenderType._
import enums.MaritalStatusType._
import enums.EducationLevelType._
import enums.WorkStatusType._

case class UserProfilePersonal(
                userProfilePersonalId: Option[Long],
                xrayTerms: String,
                aboutMe: Option[String] = None,
                pictureFile: Option[String]  = None,
                physicalFile: Option[String]  = None,
                mobile: Option[String] = None,
                alternateEmail: Option[String] = None,
                gender: Option[GenderType] = None,
                maritalStatus: Option[MaritalStatusType] = None,
                birthYear: Option[Int] = None,
                birthMonth: Option[Int] = None,
                birthDay: Option[Int] = None,
                createdAt: DateTime = new DateTime(),
                updatedAt: Option[DateTime] = None
                ) {
  def this(userProfileDTO: UserProfileDTO,
          createdAt: DateTime,
          updatedAt: Option[DateTime]) {
      this(userProfileDTO.userProfilePersonalId,
           userProfileDTO.xrayTerms,
           userProfileDTO.aboutMe,
           userProfileDTO.pictureFile,
           userProfileDTO.physicalFile,
           userProfileDTO.mobile,
           userProfileDTO.alternateEmail,
           userProfileDTO.gender,
           userProfileDTO.maritalStatus,
           userProfileDTO.birthYear,
           userProfileDTO.birthMonth,
           userProfileDTO.birthDay,
           createdAt,
           updatedAt)
  }
}

object UserProfilePersonal extends Function14[Option[Long], String, Option[String], Option[String], Option[String], Option[String], Option[String], Option[GenderType],
                                       Option[MaritalStatusType], Option[Int], Option[Int], Option[Int], DateTime, Option[DateTime], UserProfilePersonal]
{
    implicit val userProfilePersonalWrites : Writes[UserProfilePersonal] = (
            (JsPath \ "userProfilePersonalId").write[Option[Long]] and
            (JsPath \ "xrayTerms").write[String] and
            (JsPath \ "aboutMe").write[Option[String]] and
            (JsPath \ "pictureFile").write[Option[String]] and
            (JsPath \ "physicalFile").write[Option[String]] and
            (JsPath \ "mobile").write[Option[String]] and
            (JsPath \ "alternateEmail").write[Option[String]] and
            (JsPath \ "gender").write[Option[GenderType]] and
            (JsPath \ "maritalStatus").write[Option[MaritalStatusType]] and
            (JsPath \ "birthYear").write[Option[Int]] and
            (JsPath \ "birthMonth").write[Option[Int]] and
            (JsPath \ "birthDay").write[Option[Int]] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(UserProfilePersonal.unapply))
      
    implicit val userProfilePersonalReads : Reads[UserProfilePersonal] = (
            (JsPath \ "userProfilePersonalId").readNullable[Long] and
            (JsPath \ "xrayTerms").read[String] and
            (JsPath \ "aboutMe").readNullable[String] and
            (JsPath \ "pictureFile").readNullable[String] and
            (JsPath \ "physicalFile").readNullable[String] and
            (JsPath \ "mobile").readNullable[String] and
            (JsPath \ "alternateEmail").readNullable[String] and
            (JsPath \ "gender").readNullable[GenderType] and
            (JsPath \ "maritalStatus").readNullable[MaritalStatusType] and
            (JsPath \ "birthYear").readNullable[Int] and
            (JsPath \ "birthMonth").readNullable[Int] and
            (JsPath \ "birthDay").readNullable[Int] and
            (JsPath \ "createdAt").read[DateTime] and
            (JsPath \ "updatedAt").readNullable[DateTime]
    )(UserProfilePersonal)
    
    def apply(userProfileDTO: UserProfileDTO,
              createdAt: DateTime,
              updatedAt: Option[DateTime]) = new UserProfilePersonal(userProfileDTO, createdAt, updatedAt)
}
