package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * Defines a permission
 *  
 */
case class Permission(  
                 permissionId: Option[Long],
                 code: String,
                 name: String,
                 description: String,
                 createdUserId: Long,
                 createdAt: DateTime = new DateTime(),
                 updatedUserId: Option[Long] = None,
                 updatedAt: Option[DateTime] = None) {
  def this(permissionDTO: PermissionDTO, 
          createdUserId: Long, 
          createdAt: DateTime,
          updatedUserId: Option[Long], 
          updatedAt: Option[DateTime]) {
      this(permissionDTO.permissionId,
           permissionDTO.code,
           permissionDTO.name,
           permissionDTO.description,
           createdUserId,
           createdAt,
           updatedUserId,
           updatedAt)
  }
}

object Permission extends Function8[Option[Long], String, String, String, Long, DateTime, Option[Long], Option[DateTime], Permission]
{
    implicit val permissionWrites : Writes[Permission] = (
            (JsPath \ "permissionId").write[Option[Long]] and
            (JsPath \ "code").write[String] and
            (JsPath \ "name").write[String] and
            (JsPath \ "description").write[String] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(Permission.unapply))
      
    implicit val permissionReads : Reads[Permission] = (
            (JsPath \ "permissionId").readNullable[Long] and
            (JsPath \ "code").read[String] and
            (JsPath \ "name").read[String] and
            (JsPath \ "description").read[String] and
            (JsPath \ "createdUserId").read[Long] and
            (JsPath \ "createdAt").read[DateTime] and
            (JsPath \ "updatedUserId").readNullable[Long] and
            (JsPath \ "updatedAt").readNullable[DateTime]
    )(Permission)
    
    def apply(permissionDTO: PermissionDTO,
          createdUserId: Long, 
          createdAt: DateTime, 
          updatedUserId: Option[Long], 
          updatedAt: Option[DateTime]) = new Permission(permissionDTO, createdUserId, createdAt, updatedUserId, updatedAt)
}
