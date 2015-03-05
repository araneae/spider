package models.repositories

import models.tables._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.dtos._
import org.joda.time.DateTime

object SubscriptionPermissionRepository {
  
  val query = TableQuery[SubscriptionPermissions]
  
  def get(subscriptionId: Long): Seq[SubscriptionPermissionDTO] = {
    DB.withSession {
       implicit session: Session =>
        query.filter(s => s.subscriptionId === subscriptionId).list map {x => SubscriptionPermissionDTO(x)}
    }
  }
  
  def getAll(): Seq[SubscriptionPermissionDTO] = {
    DB.withSession {
       implicit session: Session =>
        query.list map {x => SubscriptionPermissionDTO(x)}
    }
  } 
  
  def find(subscriptionId: Long, permissionId: Long): Option[SubscriptionPermissionDTO] = {
    DB.withSession {
       implicit session: Session =>
          query.filter(s => s.subscriptionId === subscriptionId && s.permissionId === permissionId ).firstOption map {x => SubscriptionPermissionDTO(x)}
    }
  }
  
  def create(subscriptionPermission: SubscriptionPermission) = {
    DB.withSession {
       implicit session: Session =>
         query insert subscriptionPermission
    }
  }
  
  
   def delete(subscriptionId: Long, permissionId: Long) = {
    DB.withSession {
      implicit session =>
        query filter( d => d.subscriptionId === subscriptionId && d.permissionId === permissionId ) delete
    }
  }
  
  def update(subscriptionPermission: SubscriptionPermission) = {
    DB.withSession {
      implicit session =>
        query filter(d => d.subscriptionId === subscriptionPermission.subscriptionId && d.permissionId === subscriptionPermission.permissionId) update subscriptionPermission
    }
  }
}