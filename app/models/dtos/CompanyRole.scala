package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * Defines a company role
 *  
 */
case class CompanyRole(  
                 companyRoleId: Option[Long],
                 companyId: Long,
                 name: String,
                 description: String,
                 createdUserId: Long,
                 createdAt: DateTime = new DateTime(),
                 updatedUserId: Option[Long] = None,
                 updatedAt: Option[DateTime] = None) {
  def this(companyRoleDTO: CompanyRoleDTO, 
          createdUserId: Long, 
          createdAt: DateTime,
          updatedUserId: Option[Long], 
          updatedAt: Option[DateTime]) {
      this(companyRoleDTO.companyRoleId,
           companyRoleDTO.companyId,
           companyRoleDTO.name,
           companyRoleDTO.description,
           createdUserId,
           createdAt,
           updatedUserId,
           updatedAt)
  }
}

object CompanyRole extends Function8[Option[Long], Long, String, String, Long, DateTime, Option[Long], Option[DateTime], CompanyRole]
{
    implicit val companyRoleWrites : Writes[CompanyRole] = (
            (JsPath \ "companyRoleId").write[Option[Long]] and
            (JsPath \ "companyId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "description").write[String] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(CompanyRole.unapply))
      
    implicit val companyRoleReads : Reads[CompanyRole] = (
            (JsPath \ "companyRoleId").readNullable[Long] and
            (JsPath \ "companyId").read[Long] and
            (JsPath \ "name").read[String] and
            (JsPath \ "description").read[String] and
            (JsPath \ "createdUserId").read[Long] and
            (JsPath \ "createdAt").read[DateTime] and
            (JsPath \ "updatedUserId").readNullable[Long] and
            (JsPath \ "updatedAt").readNullable[DateTime]
    )(CompanyRole)
    
    def apply(companyRoleDTO: CompanyRoleDTO,
          createdUserId: Long, 
          createdAt: DateTime, 
          updatedUserId: Option[Long], 
          updatedAt: Option[DateTime]) = new CompanyRole(companyRoleDTO, createdUserId, createdAt, updatedUserId, updatedAt)
}
