package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * Defines subscription permission DTO
 *  
 */
case class SubscriptionPermissionDTO(  
                 subscriptionId: Long,
                 permissionId: Long) {
  def this(subscriptionPermission: SubscriptionPermission) {
      this(subscriptionPermission.subscriptionId,
           subscriptionPermission.permissionId)
  }
}

object SubscriptionPermissionDTO extends Function2[Long, Long, SubscriptionPermissionDTO]
{
    implicit val subscriptionWrites : Writes[SubscriptionPermissionDTO] = (
            (JsPath \ "subscriptionId").write[Long] and
            (JsPath \ "permissionId").write[Long]
    )(unlift(SubscriptionPermissionDTO.unapply))
      
    implicit val subscriptionReads : Reads[SubscriptionPermissionDTO] = (
            (JsPath \ "subscriptionId").read[Long] and
            (JsPath \ "permissionId").read[Long]
    )(SubscriptionPermissionDTO)
    
    def apply(subscriptionPermission: SubscriptionPermission) = new SubscriptionPermissionDTO(subscriptionPermission)
}
