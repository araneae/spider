package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.GenderType._
import enums.MaritalStatusType._
import enums.EducationLevelType._
import enums.WorkStatusType._
import utils._

case class UserProfileDTO(
                userProfilePersonalId: Option[Long],
                email: String,
                xrayTerms: String,
                aboutMe: Option[String] = None,
                pictureFile: Option[String] = None,
                physicalFile: Option[String]  = None,
                pictureUrl: String,
                mobile: Option[String] = None,
                alternateEmail: Option[String] = None,
                gender: Option[GenderType] = None,
                maritalStatus: Option[MaritalStatusType] = None,
                birthYear: Option[Int] = None,
                birthMonth: Option[Int] = None,
                birthDay: Option[Int] = None
                ) {
  def this(user: User, userProfilePersonal: UserProfilePersonal) = {
      this(userProfilePersonal.userProfilePersonalId,
           user.email,
           userProfilePersonal.xrayTerms,
           userProfilePersonal.aboutMe,
           userProfilePersonal.pictureFile,
           userProfilePersonal.physicalFile,
           userProfilePersonal.physicalFile match {
                          case Some(physicalFile) => Configuration.userProfilePictureUrl(physicalFile)
                          case None => Configuration.defaultProfilePictureUrl
                      },
           userProfilePersonal.mobile,
           userProfilePersonal.alternateEmail,
           userProfilePersonal.gender,
           userProfilePersonal.maritalStatus,
           userProfilePersonal.birthYear,
           userProfilePersonal.birthMonth,
           userProfilePersonal.birthDay)
  }
}

object UserProfileDTO extends Function14[Option[Long], String, String, Option[String], Option[String], Option[String], String, Option[String], Option[String], Option[GenderType],
                                       Option[MaritalStatusType], Option[Int], Option[Int], Option[Int], UserProfileDTO]
{
    implicit val userProfilePersonalWrites : Writes[UserProfileDTO] = (
            (JsPath \ "userProfilePersonalId").write[Option[Long]] and
            (JsPath \ "email").write[String] and
            (JsPath \ "xrayTerms").write[String] and
            (JsPath \ "aboutMe").write[Option[String]] and
            (JsPath \ "pictureFile").write[Option[String]] and
            (JsPath \ "physicalFile").write[Option[String]] and
            (JsPath \ "pictureUrl").write[String] and
            (JsPath \ "mobile").write[Option[String]] and
            (JsPath \ "alternateEmail").write[Option[String]] and
            (JsPath \ "gender").write[Option[GenderType]] and
            (JsPath \ "maritalStatus").write[Option[MaritalStatusType]] and
            (JsPath \ "birthYear").write[Option[Int]] and
            (JsPath \ "birthMonth").write[Option[Int]] and
            (JsPath \ "birthDay").write[Option[Int]]
    )(unlift(UserProfileDTO.unapply))
      
    implicit val userProfilePersonalReads : Reads[UserProfileDTO] = (
            (JsPath \ "userProfilePersonalId").readNullable[Long] and
            (JsPath \ "email").read[String] and
            (JsPath \ "xrayTerms").read[String] and
            (JsPath \ "aboutMe").readNullable[String] and
            (JsPath \ "pictureFile").readNullable[String] and
            (JsPath \ "physicalFile").readNullable[String] and
            (JsPath \ "pictureUrl").read[String] and
            (JsPath \ "mobile").readNullable[String] and
            (JsPath \ "alternateEmail").readNullable[String] and
            (JsPath \ "gender").readNullable[GenderType] and
            (JsPath \ "maritalStatus").readNullable[MaritalStatusType] and
            (JsPath \ "birthYear").readNullable[Int] and
            (JsPath \ "birthMonth").readNullable[Int] and
            (JsPath \ "birthDay").readNullable[Int]
    )(UserProfileDTO)
    
    def apply(user: User, userProfilePersonal: UserProfilePersonal) = new UserProfileDTO(user, userProfilePersonal)
}
