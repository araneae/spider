package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * Defines subscription permission
 *  
 */
case class SubscriptionPermission(  
                 subscriptionId: Long,
                 permissionId: Long,
                 createdUserId: Long,
                 createdAt: DateTime = new DateTime(),
                 updatedUserId: Option[Long] = None,
                 updatedAt: Option[DateTime] = None) {
  def this(subscriptionPermissionDTO: SubscriptionPermissionDTO, 
          createdUserId: Long, 
          createdAt: DateTime,
          updatedUserId: Option[Long], 
          updatedAt: Option[DateTime]) {
      this(subscriptionPermissionDTO.subscriptionId,
           subscriptionPermissionDTO.permissionId,
           createdUserId,
           createdAt,
           updatedUserId,
           updatedAt)
  }
}

object SubscriptionPermission extends Function6[Long, Long, Long, DateTime, Option[Long], Option[DateTime], SubscriptionPermission]
{
    implicit val subscriptionPermissionWrites : Writes[SubscriptionPermission] = (
            (JsPath \ "subscriptionId").write[Long] and
            (JsPath \ "permissionId").write[Long] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(SubscriptionPermission.unapply))
      
    implicit val subscriptionPermissionReads : Reads[SubscriptionPermission] = (
            (JsPath \ "subscriptionId").read[Long] and
            (JsPath \ "permissionId").read[Long] and
            (JsPath \ "createdUserId").read[Long] and
            (JsPath \ "createdAt").read[DateTime] and
            (JsPath \ "updatedUserId").readNullable[Long] and
            (JsPath \ "updatedAt").readNullable[DateTime]
    )(SubscriptionPermission)
    
    def apply(subscriptionPermissionDTO: SubscriptionPermissionDTO,
          createdUserId: Long, 
          createdAt: DateTime, 
          updatedUserId: Option[Long], 
          updatedAt: Option[DateTime]) = new SubscriptionPermission(subscriptionPermissionDTO, createdUserId, createdAt, updatedUserId, updatedAt)
}
