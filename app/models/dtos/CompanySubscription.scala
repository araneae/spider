package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.CompanySubscriptionStatusType._

/**
 * Defines company subscription
 *  
 */
case class CompanySubscription(  
                 companySubscriptionId: Option[Long],
                 companyId: Long,
                 subscriptionId: Long,
                 status: CompanySubscriptionStatusType,
                 createdUserId: Long,
                 createdAt: DateTime = new DateTime(),
                 updatedUserId: Option[Long] = None,
                 updatedAt: Option[DateTime] = None) {
  def this(companySubscriptionDTO: CompanySubscriptionDTO, 
          createdUserId: Long, 
          createdAt: DateTime,
          updatedUserId: Option[Long], 
          updatedAt: Option[DateTime]) {
      this(companySubscriptionDTO.companySubscriptionId,
           companySubscriptionDTO.companyId,
           companySubscriptionDTO.subscriptionId,
           companySubscriptionDTO.status,
           createdUserId,
           createdAt,
           updatedUserId,
           updatedAt)
  }
}

object CompanySubscription extends Function8[Option[Long], Long, Long, CompanySubscriptionStatusType, Long, DateTime, Option[Long], Option[DateTime], CompanySubscription]
{
    implicit val companySubscriptionWrites : Writes[CompanySubscription] = (
            (JsPath \ "companySubscriptionId").write[Option[Long]] and
            (JsPath \ "companyId").write[Long] and
            (JsPath \ "subscriptionId").write[Long] and
            (JsPath \ "status").write[CompanySubscriptionStatusType] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(CompanySubscription.unapply))
      
    implicit val companySubscriptionReads : Reads[CompanySubscription] = (
            (JsPath \ "companySubscriptionId").readNullable[Long] and
            (JsPath \ "companyId").read[Long] and
            (JsPath \ "subscriptionId").read[Long] and
            (JsPath \ "status").read[CompanySubscriptionStatusType] and
            (JsPath \ "createdUserId").read[Long] and
            (JsPath \ "createdAt").read[DateTime] and
            (JsPath \ "updatedUserId").readNullable[Long] and
            (JsPath \ "updatedAt").readNullable[DateTime]
    )(CompanySubscription)
    
    def apply(companySubscriptionDTO: CompanySubscriptionDTO,
          createdUserId: Long, 
          createdAt: DateTime, 
          updatedUserId: Option[Long], 
          updatedAt: Option[DateTime]) = new CompanySubscription(companySubscriptionDTO, createdUserId, createdAt, updatedUserId, updatedAt)
}
