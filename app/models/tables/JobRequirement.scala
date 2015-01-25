package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime
import enums.EmploymentType._
import enums.CurrencyType._
import enums.JobStatusType._

/**
 * Job requirements
 * 
 */

class JobRequirements(tag: Tag) extends Table[JobRequirement](tag, "job_requirement") {

  def jobRequirementId = column[Long]("job_requirement_id", O.PrimaryKey, O.AutoInc)
  
  def companyId = column[Long]("company_id")
  
  def code = column[String]("code")
  
  def refNumber = column[Option[String]]("ref_number")
  
  def title = column[String]("title")
  
  def employmentType = column[EmploymentType]("employment_type")
  
  def industryId = column[Long]("industry_id")
  
  def location = column[String]("location")
  
  def description = column[String]("description", O.DBType("TEXT"))

  def status = column[JobStatusType]("status")
  
  def positions = column[Int]("positions")
  
  def jobTitleId = column[Long]("job_title_id")
  
  def postDate = column[Option[DateTime]]("post_date")
  
  def jobRequirementXtnId = column[Long]("job_requirement_xtn_id")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (jobRequirementId.?, companyId, code, refNumber, title, employmentType, industryId, location, description, status,
      positions, jobTitleId, postDate, jobRequirementXtnId, createdUserId, createdAt, updatedUserId, updatedAt) <> (JobRequirement.tupled, JobRequirement.unapply)
  
  // foreign keys and indexes
  def company = foreignKey("fk_job_requirement_on_company_id", companyId, TableQuery[Companies])(_.companyId)
  
  def industry = foreignKey("fk_job_requirement_on_industry_id", industryId, TableQuery[Industries])(_.industryId)
  
  def uniqueCode = index("idx_job_requirement_on_code", (companyId, code), unique = true)
  
  def jobTitle = foreignKey("fk_job_requirement_jon_on_title_id", jobTitleId, TableQuery[JobTitles])(_.jobTitleId)
  
  def xtn = foreignKey("fk_job_requirement_on_job_requirement_xtn_id", jobRequirementXtnId, TableQuery[JobRequirementXtns])(_.jobRequirementXtnId)
  
  def createdBy = foreignKey("fk_job_requirement_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_job_requirement_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
