package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * Defines subscription
 *  
 */
case class Subscription(  
                 subscriptionId: Option[Long],
                 name: String,
                 description: String,
                 createdUserId: Long,
                 createdAt: DateTime = new DateTime(),
                 updatedUserId: Option[Long] = None,
                 updatedAt: Option[DateTime] = None) {
  def this(subscriptionDTO: SubscriptionDTO, 
          createdUserId: Long, 
          createdAt: DateTime,
          updatedUserId: Option[Long], 
          updatedAt: Option[DateTime]) {
      this(subscriptionDTO.subscriptionId,
           subscriptionDTO.name,
           subscriptionDTO.description,
           createdUserId,
           createdAt,
           updatedUserId,
           updatedAt)
  }
}

object Subscription extends Function7[Option[Long], String, String, Long, DateTime, Option[Long], Option[DateTime], Subscription]
{
    implicit val subscriptionWrites : Writes[Subscription] = (
            (JsPath \ "subscriptionId").write[Option[Long]] and
            (JsPath \ "name").write[String] and
            (JsPath \ "description").write[String] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(Subscription.unapply))
      
    implicit val subscriptionReads : Reads[Subscription] = (
            (JsPath \ "subscriptionId").readNullable[Long] and
            (JsPath \ "name").read[String] and
            (JsPath \ "description").read[String] and
            (JsPath \ "createdUserId").read[Long] and
            (JsPath \ "createdAt").read[DateTime] and
            (JsPath \ "updatedUserId").readNullable[Long] and
            (JsPath \ "updatedAt").readNullable[DateTime]
    )(Subscription)
    
    def apply(subscriptionDTO: SubscriptionDTO,
          createdUserId: Long, 
          createdAt: DateTime, 
          updatedUserId: Option[Long], 
          updatedAt: Option[DateTime]) = new Subscription(subscriptionDTO, createdUserId, createdAt, updatedUserId, updatedAt)
}
