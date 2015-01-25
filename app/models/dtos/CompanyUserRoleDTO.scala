package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * Defines a company user role DTO
 *  
 */
case class CompanyUserRoleDTO(
                 companyUserRoleId: Option[Long],
                 companyUserId: Long,
                 companyRoleId: Long) {
  def this(companyUserRole: CompanyUserRole) {
      this(companyUserRole.companyUserRoleId,
           companyUserRole.companyUserId,
           companyUserRole.companyRoleId)
  }
}

object CompanyUserRoleDTO extends Function3[Option[Long], Long, Long, CompanyUserRoleDTO]
{
    implicit val companyUserRoleDTOWrites : Writes[CompanyUserRoleDTO] = (
            (JsPath \ "companyUserRoleId").write[Option[Long]] and
            (JsPath \ "companyUserId").write[Long] and
            (JsPath \ "companyRoleId").write[Long]
    )(unlift(CompanyUserRoleDTO.unapply))
      
    implicit val companyUserRoleDTOReads : Reads[CompanyUserRoleDTO] = (
            (JsPath \ "companyUserRoleId").readNullable[Long] and
            (JsPath \ "companyUserId").read[Long] and
            (JsPath \ "companyRoleId").read[Long]
    )(CompanyUserRoleDTO)
    
    def apply(companyUserRole: CompanyUserRole) = new CompanyUserRoleDTO(companyUserRole)
}
