package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * Defines a company role permission DTO
 *  
 */
case class CompanyRolePermissionDTO(  
                 companyRolePermissionId: Option[Long],
                 companyRoleId: Long,
                 companySubscriptionId: Long,
                 permissionId: Long) {
  def this(companyRolePermission: CompanyRolePermission) {
      this(companyRolePermission.companyRolePermissionId,
           companyRolePermission.companyRoleId,
           companyRolePermission.companySubscriptionId,
           companyRolePermission.permissionId)
  }
}

object CompanyRolePermissionDTO extends Function4[Option[Long], Long, Long, Long, CompanyRolePermissionDTO]
{
    implicit val companyRolePermissionDTOWrites : Writes[CompanyRolePermissionDTO] = (
            (JsPath \ "companyRolePermissionId").write[Option[Long]] and
            (JsPath \ "companyRoleId").write[Long] and
            (JsPath \ "companySubscriptionId").write[Long] and
            (JsPath \ "permissionId").write[Long]
    )(unlift(CompanyRolePermissionDTO.unapply))
      
    implicit val companyRolePermissionDTOReads : Reads[CompanyRolePermissionDTO] = (
            (JsPath \ "companyRolePermissionId").readNullable[Long] and
            (JsPath \ "companyRoleId").read[Long] and
            (JsPath \ "companySubscriptionId").read[Long] and
            (JsPath \ "permissionId").read[Long]
    )(CompanyRolePermissionDTO)
    
    def apply(companyRolePermission: CompanyRolePermission) = new CompanyRolePermissionDTO(companyRolePermission)
}
