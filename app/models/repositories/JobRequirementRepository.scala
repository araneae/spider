package models.repositories

import models.tables._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.dtos._
import org.joda.time.DateTime

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

