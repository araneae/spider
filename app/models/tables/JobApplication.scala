package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime
import enums.RelocationType._
import enums.TravelingType._

/**
 * Job application
 * 
 */

class JobApplications(tag: Tag) extends Table[JobApplication](tag, "job_application") {

  def jobApplicationId = column[Long]("job_application_id", O.PrimaryKey, O.AutoInc)
  
  def companyId = column[Long]("company_id")
  
  def jobRequirementId = column[Long]("job_requirement_id")
  
  def phone = column[String]("phone")
  
  def availableInWeeks = column[Int]("available_in_weeks")
  
  def relocation = column[RelocationType]("relocation")
  
  def traveling = column[TravelingType]("traveling")
  
  def message = column[String]("message", O.DBType("TEXT"))

  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (jobApplicationId.?, companyId, jobRequirementId, phone, availableInWeeks, relocation, traveling, message,
                            createdUserId, createdAt, updatedUserId, updatedAt) <> (JobApplication.tupled, JobApplication.unapply)
  
  // foreign keys and indexes
  def company = foreignKey("fk_job_application_on_company_id", companyId, TableQuery[Companies])(_.companyId)
  
  def jobRequirement = foreignKey("fk_job_application_on_job_requirement_id", jobRequirementId, TableQuery[JobRequirements])(_.jobRequirementId)
  
  def createdBy = foreignKey("fk_job_application_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_job_application_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
