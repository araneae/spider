package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime
import enums.EmploymentType._
import enums.CurrencyType._
import enums.SalaryTermType._
import enums.JobStatusType._

/**
 * Job requirements
 * 
 */

class JobRequirements(tag: Tag) extends Table[JobRequirement](tag, "job_requirement") {

  def jobRequirementId = column[Long]("job_requirement_id", O.PrimaryKey, O.AutoInc)
  
  def companyId = column[Long]("company_id", O.NotNull)
  
  def code = column[String]("code", O.NotNull)
  
  def refNumber = column[Option[String]]("ref_number", O.Nullable)
  
  def title = column[String]("title", O.NotNull)
  
  def employmentType = column[EmploymentType]("employment_type", O.NotNull)
  
  def industryId = column[Long]("industry_id", O.NotNull)
  
  def location = column[String]("location", O.NotNull)
  
  def salaryMin = column[Double]("salary_min", O.NotNull)
  
  def salaryMax = column[Double]("salary_max", O.NotNull)

  def currency = column[CurrencyType]("currency", O.NotNull)

  def salaryTerm = column[SalaryTermType]("salary_term", O.NotNull)
  
  def description = column[String]("description", O.NotNull, O.DBType("TEXT"))

  def status = column[JobStatusType]("status", O.Nullable)
  
  def positions = column[Int]("positions", O.NotNull)
  
  def jobTitleId = column[Long]("job_title_id", O.NotNull)
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Option[Long]]("updated_user_id", O.Nullable)
  
  def updatedAt = column[Option[DateTime]]("updated_at", O.Nullable)
  
  override def * = (jobRequirementId.?, companyId, code, refNumber, title, employmentType, industryId, location, salaryMin, salaryMax, currency, 
      salaryTerm, description, status, positions, jobTitleId, createdUserId, createdAt, updatedUserId, updatedAt) <> (JobRequirement.tupled, JobRequirement.unapply)
  
  // foreign keys and indexes
  def company = foreignKey("fk_job_requirement_on_company_id", companyId, TableQuery[Companies])(_.companyId)
  
  def industry = foreignKey("fk_job_requirement_on_industry_id", industryId, TableQuery[Industries])(_.industryId)
  
  def uniqueCode = index("idx_job_requirement_on_code_unique", (companyId, code), unique = true)
  
  def jobTitle = foreignKey("fk_job_requirement_jon_on_title_id", jobTitleId, TableQuery[JobTitles])(_.jobTitleId)
  
  def createdBy = foreignKey("fk_job_requirement_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_job_requirement_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
