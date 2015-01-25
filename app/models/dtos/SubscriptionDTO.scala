package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * Defines a Subscription DTO
 *  
 */
case class SubscriptionDTO(  
                 subscriptionId: Option[Long],
                 name: String,
                 description: String) {
  def this(subscription: Subscription) {
      this(subscription.subscriptionId,
           subscription.name,
           subscription.description)
  }
}

object SubscriptionDTO extends Function3[Option[Long], String, String, SubscriptionDTO]
{
    implicit val subscriptionDTOWrites : Writes[SubscriptionDTO] = (
            (JsPath \ "subscriptionId").write[Option[Long]] and
            (JsPath \ "name").write[String] and
            (JsPath \ "description").write[String]
    )(unlift(SubscriptionDTO.unapply))
      
    implicit val subscriptionDTOReads : Reads[SubscriptionDTO] = (
            (JsPath \ "subscriptionId").readNullable[Long] and
            (JsPath \ "name").read[String] and
            (JsPath \ "description").read[String]
    )(SubscriptionDTO)
    
    def apply(role: Subscription) = new SubscriptionDTO(role)
}
