package models.repositories

import models.tables._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.dtos._
import org.joda.time.DateTime

object IndustryRepository {
  
  val query = TableQuery[Industries]
  
  def create(industry: Industry) : Long = {
    DB.withSession {
       implicit session: Session =>
         query returning query.map(_.industryId) += industry
         
    }
  }
  
  def udate(industry: Industry, userId: Long) = {
    DB.withSession {
       implicit session: Session =>
         val q = for {
           q <- query filter(_.industryId === industry.industryId)
         } yield (q.name, q.description, q.updatedUserId, q.updatedAt)
         
         q update((industry.name, industry.description, Some(userId), Some(new DateTime())))
    }
  }

  def find(industryId: Long): Option[Industry] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.industryId === industryId) firstOption
    }
  }
  
  def findAll(): Seq[Industry] = {
    DB.withSession {
       implicit session: Session =>
          query list
    }
  }
  
  def delete(industryId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.industryId === industryId) delete
    }
  }
}

