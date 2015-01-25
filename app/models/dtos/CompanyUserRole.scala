package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * Defines a company user role
 *  
 */
case class CompanyUserRole(  
                 companyUserRoleId: Option[Long],
                 companyUserId: Long,
                 companyRoleId: Long,
                 createdUserId: Long,
                 createdAt: DateTime = new DateTime(),
                 updatedUserId: Option[Long] = None,
                 updatedAt: Option[DateTime] = None) {
  def this(companyUserRoleDTO: CompanyUserRoleDTO, 
          createdUserId: Long, 
          createdAt: DateTime,
          updatedUserId: Option[Long], 
          updatedAt: Option[DateTime]) {
      this(companyUserRoleDTO.companyUserRoleId,
    		  companyUserRoleDTO.companyUserId,
           companyUserRoleDTO.companyRoleId,
           createdUserId,
           createdAt,
           updatedUserId,
           updatedAt)
  }
}

object CompanyUserRole extends Function7[Option[Long], Long, Long, Long, DateTime, Option[Long], Option[DateTime], CompanyUserRole]
{
    implicit val companyUserRoleWrites : Writes[CompanyUserRole] = (
            (JsPath \ "companyUserRoleId").write[Option[Long]] and
            (JsPath \ "companyUserId").write[Long] and
            (JsPath \ "companyRoleId").write[Long] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(CompanyUserRole.unapply))
      
    implicit val companyUserRoleReads : Reads[CompanyUserRole] = (
            (JsPath \ "companyUserRoleId").readNullable[Long] and
            (JsPath \ "companyUserId").read[Long] and
            (JsPath \ "companyRoleId").read[Long] and
            (JsPath \ "createdUserId").read[Long] and
            (JsPath \ "createdAt").read[DateTime] and
            (JsPath \ "updatedUserId").readNullable[Long] and
            (JsPath \ "updatedAt").readNullable[DateTime]
    )(CompanyUserRole)
    
    def apply(companyUserRoleDTO: CompanyUserRoleDTO,
          createdUserId: Long, 
          createdAt: DateTime, 
          updatedUserId: Option[Long], 
          updatedAt: Option[DateTime]) = new CompanyUserRole(companyUserRoleDTO, createdUserId, createdAt, updatedUserId, updatedAt)
}
