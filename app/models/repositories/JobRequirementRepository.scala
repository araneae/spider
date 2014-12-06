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
  
  def update(jobRequirement: JobRequirement, userId: Long) = {
    DB.withSession {
       implicit session: Session =>
         val q = for {
            r <- query.filter(_.jobRequirementId === jobRequirement.jobRequirementId)
          } yield (r.code, r.refNumber, r.title, r.employmentType, r.industryId, r.location, r.salaryMin, r.salaryMax,
              r.currency, r.salaryTerm, r.description, r.status, r.positions, r.jobTitleId, r.description, r.updatedUserId, r.updatedAt)
          
          q update((jobRequirement.code, jobRequirement.refNumber, jobRequirement.title, jobRequirement.employmentType, 
              jobRequirement.industryId, jobRequirement.location, jobRequirement.salaryMin, jobRequirement.salaryMax,
              jobRequirement.currency, jobRequirement.salaryTerm, jobRequirement.description, jobRequirement.status, 
              jobRequirement.positions, jobRequirement.jobTitleId, jobRequirement.description, Some(userId), Some(new DateTime())))
    }
  }
  
  def find(jobRequirementId: Long): Option[JobRequirementDTO] = {
    DB.withSession {
       implicit session: Session =>
          val q = for {
             r <- query filter (_.jobRequirementId === jobRequirementId)
           } 
          yield (r.jobRequirementId, r.code, r.refNumber, r.title, r.employmentType, r.industryId, r.location, r.salaryMin, 
               r.salaryMax, r.currency, r.salaryTerm, r.description, r.status, r.positions, r.jobTitleId)

          val result = q.list.map{case (jobRequirementId, code, refNumber, title, employmentType, industryId, location, salaryMin, 
                   salaryMax, currency, salaryTerm, description, status, positions, jobTitleId) => 
                 JobRequirementDTO(Some(jobRequirementId), 1, code, refNumber, title, employmentType, industryId, location, salaryMin, 
                   salaryMax, currency, salaryTerm, description, status, positions, jobTitleId)}
           
          if (result.length > 0) Some(result(0))
          else None
    }
  }
  
  def findByCode(code: String): Option[JobRequirement] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.code === code) firstOption
    }
  }
  
  def findAll(): Seq[JobRequirementDTO] = {
    DB.withSession {
       implicit session: Session =>
         val q = for {
             r <- query
           } yield (r.jobRequirementId, r.code, r.refNumber, r.title, r.employmentType, r.industryId, r.location, r.salaryMin, 
               r.salaryMax, r.currency, r.salaryTerm, r.description, r.status, r.positions, r.jobTitleId)
           
           q.list.map{case (jobRequirementId, code, refNumber, title, employmentType, industryId, location, salaryMin, 
                   salaryMax, currency, salaryTerm, description, status, positions, jobTitleId) => 
                 JobRequirementDTO(Some(jobRequirementId), 1, code, refNumber, title, employmentType, industryId, location, salaryMin, 
                   salaryMax, currency, salaryTerm, description, status, positions, jobTitleId)}
    }
  }
  
  def delete(jobRequirementId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter(_.jobRequirementId === jobRequirementId).delete
    }
  }
}

