package models.repositories

import models.tables._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.dtos._
import org.joda.time.DateTime
import enums.JobStatusType._
import enums._

object JobRequirementRepository {
  
  val query = TableQuery[JobRequirements]
  
  def create(jobRequirement: JobRequirement) = {
    DB.withSession {
       implicit session: Session =>
         query returning query.map(_.jobRequirementId) += jobRequirement
    }
  }
  
  def update(jobRequirement: JobRequirement) = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.jobRequirementId === jobRequirement.jobRequirementId) update jobRequirement
    }
  }
  
  def updateStatus(jobRequirementId: Long, userId: Long, status: JobStatusType) = {
    DB.withSession {
       implicit session: Session =>
          val q = for {
            r <- query filter(_.jobRequirementId === jobRequirementId)
          } yield (r.status, r.updatedUserId, r.postDate, r.updatedAt)
          
          val postDate = if (status == JobStatusType.POSTED) Some(new DateTime()) else None
          q update ((status, Some(userId), postDate, Some(new DateTime())))
    }
  }
  
  def find(jobRequirementId: Long): Option[JobRequirement] = {
    DB.withSession {
       implicit session: Session =>
         query filter (_.jobRequirementId === jobRequirementId) firstOption
    }
  }
  
  def findByCode(code: String): Option[JobRequirement] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.code === code) firstOption
    }
  }
  
  def getAll(companyId: Long): Seq[JobRequirementDTO] = {
    DB.withSession {
       implicit session: Session =>
         val q = for {
             r <- query filter(_.companyId === companyId)
             rx <- r.xtn
         } yield (r, rx)
           
         q.list.map{case (r, rx) => JobRequirementDTO(r, rx)}
    }
  }
  
  def getAllJobDTOByRequirementIds(jobRequirementIds: Seq[Long]): Seq[JobDTO] = {
    DB.withSession {
       implicit session: Session =>
         val q = for {
             r <- query filter(_.jobRequirementId inSet jobRequirementIds)
             c <- r.company
             i <- r.industry
             j <- r.jobTitle
             rx <- r.xtn
         } yield (c, i, j, r, rx)
           
         q.list.map{case (c, i, j, r, rx) => JobDTO(c, i, j, r, rx)}
    }
  }
  
  def getJobDTOByRequirementId(jobRequirementId: Long): Option[JobDTO] = {
    DB.withSession {
       implicit session: Session =>
         val q = for {
             r <- query filter(_.jobRequirementId === jobRequirementId)
             c <- r.company
             i <- r.industry
             j <- r.jobTitle
             rx <- r.xtn
         } yield (c, i, j, r, rx)
           
         q.firstOption.map{case (c, i, j, r, rx) => JobDTO(c, i, j, r, rx)}
    }
  }
  
  def get(jobRequirementId: Long): Option[JobRequirementDTO] = {
    DB.withSession {
       implicit session: Session =>
         val q = for {
           r <- query filter (_.jobRequirementId === jobRequirementId)
           rx <- r.xtn
         } yield(r, rx)
         
         q.firstOption.map{ case(r, rx) => JobRequirementDTO(r, rx)}
    }
  }
  
  def delete(jobRequirementId: Long) = {
    DB.withSession {
       implicit session: Session =>
           query filter(_.jobRequirementId === jobRequirementId) delete
    }
  }
}

