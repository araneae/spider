package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.CompanySubscriptionStatusType._

/**
 * Defines company subscription DTO
 *  
 */
case class CompanySubscriptionDTO(  
                 companySubscriptionId: Option[Long],
                 companyId: Long,
                 subscriptionId: Long,
                 status: CompanySubscriptionStatusType) {
  def this(subscriptionPermission: CompanySubscription) {
      this(subscriptionPermission.companySubscriptionId,
           subscriptionPermission.companyId,
           subscriptionPermission.subscriptionId,
           subscriptionPermission.status)
  }
}

object CompanySubscriptionDTO extends Function4[Option[Long], Long, Long, CompanySubscriptionStatusType, CompanySubscriptionDTO]
{
    implicit val subscriptionPermissionWrites : Writes[CompanySubscriptionDTO] = (
            (JsPath \ "companySubscriptionId").write[Option[Long]] and
            (JsPath \ "companyId").write[Long] and
            (JsPath \ "subscriptionId").write[Long] and
            (JsPath \ "status").write[CompanySubscriptionStatusType]
    )(unlift(CompanySubscriptionDTO.unapply))
      
    implicit val subscriptionPermissionReads : Reads[CompanySubscriptionDTO] = (
            (JsPath \ "subscriptionPermissionId").readNullable[Long] and
            (JsPath \ "companyId").read[Long] and
            (JsPath \ "subscriptionId").read[Long] and
            (JsPath \ "status").read[CompanySubscriptionStatusType]
    )(CompanySubscriptionDTO)
    
    def apply(subscriptionPermission: CompanySubscription,
          createdUserId: Long, 
          createdAt: DateTime, 
          updatedUserId: Option[Long], 
          updatedAt: Option[DateTime]) = new CompanySubscriptionDTO(subscriptionPermission)
}
