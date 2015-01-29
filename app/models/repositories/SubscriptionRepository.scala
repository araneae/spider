package models.repositories

import models.tables._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.dtos._
import org.joda.time.DateTime

object SubscriptionRepository {
  
  val query = TableQuery[Subscriptions]
  
  def get(subscriptionId: Long): Option[SubscriptionDTO] = {
    DB.withSession {
       implicit session: Session =>
          query.filter(_.subscriptionId === subscriptionId).firstOption map {x => SubscriptionDTO(x)}
    }
  }
  
  def getAll: Seq[SubscriptionDTO] = {
    DB.withSession {
       implicit session: Session =>
          query.list map { x => SubscriptionDTO(x)}
    }
  }
  
  def find(subscriptionId: Long): Option[Subscription] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.subscriptionId === subscriptionId) firstOption
    }
  }
  
  def create(subscription: Subscription): Long = {
    DB.withSession {
      implicit session =>
        query returning query.map(_.subscriptionId) += subscription
    }
  }
  
   def delete(subscriptionId: Long) = {
    DB.withSession {
      implicit session =>
        query filter (_.subscriptionId === subscriptionId) delete
    }
  }
  
  def findByUserId(userId: Long): Option[Subscription] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.createdUserId === userId) firstOption
    }
  }
  
  def update(subscription: Subscription) = {
    DB.withSession {
      implicit session =>
        query filter(_.subscriptionId === subscription.subscriptionId) update subscription
    }
  }
}