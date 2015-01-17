package models.repositories

import models.tables._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.dtos._
import org.joda.time.DateTime
import enums.JobStatusType._
import enums._

object JobApplicationRepository {
  
  val query = TableQuery[JobApplications]
  
  def create(jobApplication: JobApplication) = {
    DB.withSession {
       implicit session: Session =>
         query returning query.map(_.jobApplicationId) += jobApplication
    }
  }
  
  def update(jobApplication: JobApplication) = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.jobApplicationId === jobApplication.jobApplicationId) update jobApplication
    }
  }
  
  def find(jobApplicationId: Long): Option[JobApplication] = {
    DB.withSession {
       implicit session: Session =>
         query filter (_.jobApplicationId === jobApplicationId) firstOption
    }
  }
  
  def delete(jobApplicationId: Long) = {
    DB.withSession {
       implicit session: Session =>
           query filter(_.jobApplicationId === jobApplicationId) delete
    }
  }
}

