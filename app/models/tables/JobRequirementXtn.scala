package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime
import enums.PaymentTermType._
import enums.CurrencyType._
import enums.TaxTermType._
import enums.SalaryType._
import enums.BackgroundCheckType._

/**
 * Job requirements extension
 * 
 */

class JobRequirementXtns(tag: Tag) extends Table[JobRequirementXtn](tag, "job_requirement_xtn") {

  def jobRequirementXtnId = column[Long]("job_requirement_xtn_id", O.PrimaryKey, O.AutoInc)
  
  def targetStartDate = column[Option[DateTime]]("target_start_date")
  
  def targetEndDate = column[Option[DateTime]]("target_end_date")
  
  def salaryType = column[SalaryType]("salary_type")
  
  def salaryMin = column[Option[Double]]("salary_min")
  
  def salaryMax = column[Option[Double]]("salary_max")
  
  def currency = column[CurrencyType]("currency")
  
  def taxTerm = column[TaxTermType]("tax_term")
  
  def paymentTerm = column[PaymentTermType]("payment_term")
  
  def backgroundCheck = column[BackgroundCheckType]("background_check")

  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (jobRequirementXtnId.?, targetStartDate, targetEndDate, salaryType, salaryMin, salaryMax, currency, taxTerm, paymentTerm,
      backgroundCheck, createdUserId, createdAt, updatedUserId, updatedAt) <> (JobRequirementXtn.tupled, JobRequirementXtn.unapply)
  
  // foreign keys and indexes
  def createdBy = foreignKey("fk_job_requirement_xtn_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_job_requirement_xtn_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
