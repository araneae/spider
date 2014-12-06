package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.CompanyUserStatusType._
import enums.CompanyUserType._

/**
 * Defines company users
 *  
 */
case class CompanyUser(
                 companyUserId: Option[Long],
                 companyId: Long,
                 firstName: String,
                 middleName: Option[String],
                 lastName: String,
                 email: String,
                 status: CompanyUserStatusType,
                 userType: CompanyUserType,
                 createdUserId: Long,
                 createdAt: DateTime = new DateTime(),
                 updatedUserId: Option[Long] = None,
                 updatedAt: Option[DateTime] = None)

object CompanyUser extends Function12[Option[Long], Long, String, Option[String], String, String, CompanyUserStatusType, CompanyUserType, Long, DateTime, Option[Long], Option[DateTime], CompanyUser]
{
    implicit val companyUserWrites : Writes[CompanyUser] = (
            (JsPath \ "companyUserId").write[Option[Long]] and
            (JsPath \ "companyId").write[Long] and
            (JsPath \ "firstName").write[String] and
            (JsPath \ "middleName").write[Option[String]] and
            (JsPath \ "lastName").write[String] and
            (JsPath \ "email").write[String] and
            (JsPath \ "status").write[CompanyUserStatusType] and
            (JsPath \ "userType").write[CompanyUserType] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(CompanyUser.unapply))
      
    implicit val companyUserReads : Reads[CompanyUser] = (
            (JsPath \ "companyUserId").readNullable[Long] and
            (JsPath \ "companyId").read[Long] and
            (JsPath \ "firstName").read[String] and
            (JsPath \ "middleName").readNullable[String] and
            (JsPath \ "lastName").read[String] and
            (JsPath \ "email").read[String] and
            (JsPath \ "status").read[CompanyUserStatusType] and
            (JsPath \ "userType").read[CompanyUserType] and
            (JsPath \ "createdUserId").read[Long] and
            (JsPath \ "createdAt").read[DateTime] and
            (JsPath \ "updatedUserId").readNullable[Long] and
            (JsPath \ "updatedAt").readNullable[DateTime]
    )(CompanyUser)
    
}
