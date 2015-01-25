package models.repositories

import models.tables._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.dtos._
import org.joda.time.DateTime

object PermissionRepository {
  
  val query = TableQuery[Permissions]
  
  def get(permissionId: Long): Option[PermissionDTO] = {
    DB.withSession {
       implicit session: Session =>
          query.filter(_.permissionId === permissionId).firstOption map {x => PermissionDTO(x)}
    }
  }
  
  def getAll: Seq[PermissionDTO] = {
    DB.withSession {
       implicit session: Session =>
          query.list map { x => PermissionDTO(x)}
    }
  }
  
  def find(permissionId: Long): Option[Permission] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.permissionId === permissionId) firstOption
    }
  }
  
  def create(permission: Permission): Long = {
    DB.withSession {
      implicit session =>
        query returning query.map(_.permissionId) += permission
    }
  }
  
   def delete(permissionId: Long) = {
    DB.withSession {
      implicit session =>
        query filter (_.permissionId === permissionId) delete
    }
  }
  
  def findByUserId(userId: Long): Option[Permission] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.createdUserId === userId) firstOption
    }
  }
  
  def update(permission: Permission) = {
    DB.withSession {
      implicit session =>
        query filter(_.permissionId === permission.permissionId) update permission
    }
  }
}