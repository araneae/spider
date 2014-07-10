package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._

object UserTagRepository {
  
  val query = TableQuery[UserTags]
  
  def create(userTag: UserTag) = {
    DB.withSession {
       implicit session: Session =>
         query returning query.map(_.id) += userTag
    }
  }
  
  def findAll(userId: Long): Seq[UserTag] = {
    DB.withSession {
      implicit session =>
       query.filter(_.userId === userId).list
    }
  }
  
  def udate(userTag: UserTag) = {
    DB.withSession {
       implicit session: Session =>
         query filter(_.id === userTag.id) update userTag 
    }
  }
  
  def delete(id: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter( u => u.id === id).delete
    }
  }
}

