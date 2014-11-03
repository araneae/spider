package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import org.joda.time.DateTime

object UserTagRepository {
  
  val query = TableQuery[UserTags]
  
  def create(userTag: UserTag) = {
    DB.withSession {
       implicit session: Session =>
         query returning query.map(_.userTagId) += userTag
    }
  }
  
  def findAll(userId: Long): Seq[UserTag] = {
    DB.withSession {
      implicit session =>
       query.filter(_.userId === userId).list
    }
  }
  
  def udate(userTag: UserTag, userId: Long) = {
    DB.withSession {
       implicit session: Session =>
         val q = for { 
           ut <-  query filter(_.userTagId === userTag.userTagId)
         } yield (ut.name, ut.updatedUserId, ut.updatedAt)
         
         q.update((userTag.name, Some(userId), Some(new DateTime()))) 
    }
  }
  
  def delete(userTagId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter( u => u.userTagId === userTagId).delete
    }
  }
}

