package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.CompanyUserStatusType._
import enums.CompanyUserType._

/**
 * Defines company user DTO
 *  
 */
case class CompanyUserDTO(
                 companyUserId: Option[Long],
                 companyId: Long,
                 firstName: String,
                 middleName: Option[String],
                 lastName: String,
                 email: String,
                 status: CompanyUserStatusType,
                 userType: CompanyUserType) {
  def this(companyUser: CompanyUser) {
      this(companyUser.companyUserId,
           companyUser.companyId,
           companyUser.firstName,
           companyUser.middleName,
           companyUser.lastName,
           companyUser.email,
           companyUser.status,
           companyUser.userType)
  }
}

object CompanyUserDTO extends Function8[Option[Long], Long, String, Option[String], String, String, CompanyUserStatusType, CompanyUserType, CompanyUserDTO]
{
    implicit val companyUserWrites : Writes[CompanyUserDTO] = (
            (JsPath \ "companyUserId").write[Option[Long]] and
            (JsPath \ "companyId").write[Long] and
            (JsPath \ "firstName").write[String] and
            (JsPath \ "middleName").write[Option[String]] and
            (JsPath \ "lastName").write[String] and
            (JsPath \ "email").write[String] and
            (JsPath \ "status").write[CompanyUserStatusType] and
            (JsPath \ "userType").write[CompanyUserType]
    )(unlift(CompanyUserDTO.unapply))
      
    implicit val companyUserReads : Reads[CompanyUserDTO] = (
            (JsPath \ "companyUserId").readNullable[Long] and
            (JsPath \ "companyId").read[Long] and
            (JsPath \ "firstName").read[String] and
            (JsPath \ "middleName").readNullable[String] and
            (JsPath \ "lastName").read[String] and
            (JsPath \ "email").read[String] and
            (JsPath \ "status").read[CompanyUserStatusType] and
            (JsPath \ "userType").read[CompanyUserType]
    )(CompanyUserDTO)
    
    def apply(companyUser: CompanyUser) = new CompanyUserDTO(companyUser)
}
