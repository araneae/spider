package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * Defines a permission DTO
 *  
 */
case class PermissionDTO(  
                 permissionId: Option[Long],
                 code: String,
                 name: String,
                 description: String) {
  def this(permission: Permission) {
      this(permission.permissionId,
           permission.code,
           permission.name,
           permission.description)
  }
}

object PermissionDTO extends Function4[Option[Long], String, String, String, PermissionDTO]
{
    implicit val permissionDTOWrites : Writes[PermissionDTO] = (
            (JsPath \ "permissionId").write[Option[Long]] and
            (JsPath \ "code").write[String] and
            (JsPath \ "name").write[String] and
            (JsPath \ "description").write[String]
    )(unlift(PermissionDTO.unapply))
      
    implicit val permissionDTOReads : Reads[PermissionDTO] = (
            (JsPath \ "permissionId").readNullable[Long] and
            (JsPath \ "code").read[String] and
            (JsPath \ "name").read[String] and
            (JsPath \ "description").read[String]
    )(PermissionDTO)
    
    def apply(permission: Permission) = new PermissionDTO(permission)
}
