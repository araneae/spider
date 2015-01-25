package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * Defines a company role DTO
 *  
 */
case class CompanyRoleDTO(  
                 companyRoleId: Option[Long],
                 companyId: Long,
                 name: String,
                 description: String) {
  def this(companyRole: CompanyRole) {
      this(companyRole.companyRoleId,
           companyRole.companyId,
           companyRole.name,
           companyRole.description)
  }
}

object CompanyRoleDTO extends Function4[Option[Long], Long, String, String, CompanyRoleDTO]
{
    implicit val companyRoleDTOWrites : Writes[CompanyRoleDTO] = (
            (JsPath \ "companyRoleId").write[Option[Long]] and
            (JsPath \ "companyId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "description").write[String]
    )(unlift(CompanyRoleDTO.unapply))
      
    implicit val companyRoleDTOReads : Reads[CompanyRoleDTO] = (
            (JsPath \ "companyRoleId").readNullable[Long] and
            (JsPath \ "companyId").read[Long] and
            (JsPath \ "name").read[String] and
            (JsPath \ "description").read[String]
    )(CompanyRoleDTO)
    
    def apply(companyRole: CompanyRole) = new CompanyRoleDTO(companyRole)
}
