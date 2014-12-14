package models.repositories

import models.tables._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.dtos._
import org.joda.time.DateTime

object JobRequirementXtnRepository {
  
  val query = TableQuery[JobRequirementXtns]
  
  def create(jobRequirementXtn: JobRequirementXtn) = {
    DB.withSession {
       implicit session: Session =>
         query returning query.map(_.jobRequirementXtnId) += jobRequirementXtn
    }
  }
  
  def update(jobRequirementXtn: JobRequirementXtn) = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.jobRequirementXtnId === jobRequirementXtn.jobRequirementXtnId) update jobRequirementXtn
    }
  }
  
  def find(jobRequirementXtnId: Long): Option[JobRequirementXtn] = {
    DB.withSession {
       implicit session: Session =>
         query filter (_.jobRequirementXtnId === jobRequirementXtnId) firstOption
    }
  }
  
  def delete(jobRequirementXtnId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.jobRequirementXtnId === jobRequirementXtnId) delete
    }
  }
}

