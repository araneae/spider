package models.repositories

import models.tables._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.dtos._
import org.joda.time.DateTime

object JobTitleRepository {
  
  val query = TableQuery[JobTitles]
  
  def create(jobTitle: JobTitle) = {
    DB.withSession {
       implicit session: Session =>
         query returning query.map(_.jobTitleId) += jobTitle
    }
  }
  
  def update(jobTitle: JobTitle, userId: Long) = {
    DB.withSession {
       implicit session: Session =>
         val q = for {
            d <- query.filter(_.jobTitleId === jobTitle.jobTitleId)
          } yield (d.code, d.name, d.description, d.updatedUserId, d.updatedAt)
          
          q update((jobTitle.code, jobTitle.name, jobTitle.description, Some(userId), Some(new DateTime())))
    }
  }
  
  def find(jobTitleId: Long): Option[JobTitleFull] = {
    DB.withSession {
       implicit session: Session =>
          val q = for {
             s <- query filter (_.jobTitleId === jobTitleId)
           } yield (s.jobTitleId, s.name, s.code, s.description)
           
          val result = q.list.map{case (jobTitleId, name, code, description) => 
                 JobTitleFull(jobTitleId, name, code, description)}
           
          if (result.length > 0) Some(result(0))
          else None
    }
  }
  
  def findByCode(code: String): Option[JobTitle] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.code === code) firstOption
    }
  }
  
  def findAll(): Seq[JobTitleFull] = {
    DB.withSession {
       implicit session: Session =>
         val q = for {
             s <- query
           } yield (s.jobTitleId, s.name, s.code, s.description)
           
           q.list.map{case (jobTitleId, name, code, description) => JobTitleFull(jobTitleId, name, code, description)}
    }
  }
  
  def delete(jobTitleId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter(_.jobTitleId === jobTitleId).delete
    }
  }
}

