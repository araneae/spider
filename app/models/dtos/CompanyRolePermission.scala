package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * Defines a company role permission
 *  
 */
case class CompanyRolePermission(  
                 companyRolePermissionId: Option[Long],
                 companyRoleId: Long,
                 companySubscriptionId: Long,
                 permissionId: Long,
                 createdUserId: Long,
                 createdAt: DateTime = new DateTime(),
                 updatedUserId: Option[Long] = None,
                 updatedAt: Option[DateTime] = None) {
  def this(companyRolePermissionDTO: CompanyRolePermissionDTO, 
          createdUserId: Long, 
          createdAt: DateTime,
          updatedUserId: Option[Long], 
          updatedAt: Option[DateTime]) {
      this(companyRolePermissionDTO.companyRolePermissionId,
           companyRolePermissionDTO.companyRoleId,
           companyRolePermissionDTO.companySubscriptionId,
           companyRolePermissionDTO.permissionId,
           createdUserId,
           createdAt,
           updatedUserId,
           updatedAt)
  }
}

object CompanyRolePermission extends Function8[Option[Long], Long, Long, Long, Long, DateTime, Option[Long], Option[DateTime], CompanyRolePermission]
{
    implicit val companyRolePermissionWrites : Writes[CompanyRolePermission] = (
            (JsPath \ "companyRolePermissionId").write[Option[Long]] and
            (JsPath \ "companyRoleId").write[Long] and
            (JsPath \ "companySubscriptionId").write[Long] and
            (JsPath \ "permissionId").write[Long] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(CompanyRolePermission.unapply))
      
    implicit val companyRolePermissionReads : Reads[CompanyRolePermission] = (
            (JsPath \ "companyRolePermissionId").readNullable[Long] and
            (JsPath \ "companyRoleId").read[Long] and
            (JsPath \ "companySubscriptionId").read[Long] and
            (JsPath \ "permissionId").read[Long] and
            (JsPath \ "createdUserId").read[Long] and
            (JsPath \ "createdAt").read[DateTime] and
            (JsPath \ "updatedUserId").readNullable[Long] and
            (JsPath \ "updatedAt").readNullable[DateTime]
    )(CompanyRolePermission)
    
    def apply(companyRolePermissionDTO: CompanyRolePermissionDTO,
          createdUserId: Long, 
          createdAt: DateTime, 
          updatedUserId: Option[Long], 
          updatedAt: Option[DateTime]) = new CompanyRolePermission(companyRolePermissionDTO, createdUserId, createdAt, updatedUserId, updatedAt)
}
