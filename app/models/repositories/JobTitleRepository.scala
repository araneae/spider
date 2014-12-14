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
  
  def update(jobTitle: JobTitle) = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.jobTitleId === jobTitle.jobTitleId) update jobTitle
    }
  }
  
  def find(jobTitleId: Long): Option[JobTitle] = {
    DB.withSession {
       implicit session: Session =>
         query filter (_.jobTitleId === jobTitleId) firstOption
    }
  }
  
  def findAll(companyId: Long): Seq[JobTitleDTO] = {
    DB.withSession {
       implicit session: Session =>
         val q = for {
             s <- query filter (_.companyId ===  companyId)
           } yield (s)
           
           q.list.map{case (s) => JobTitleDTO(s)}
    }
  }
  
  def delete(jobTitleId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter(_.jobTitleId === jobTitleId).delete
    }
  }
}

