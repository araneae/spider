package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

/**
 * Job titles
 * 
 */

class JobTitles(tag: Tag) extends Table[JobTitle](tag, "job_title") {

  def jobTitleId = column[Long]("job_title_id", O.PrimaryKey, O.AutoInc)
  
  def companyId = column[Long]("company_id", O.NotNull)
  
  def name = column[String]("name", O.NotNull)
  
  def code = column[String]("code", O.NotNull)
  
  def description = column[Option[String]]("description", O.Nullable)
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Option[Long]]("updated_user_id", O.Nullable)
  
  def updatedAt = column[Option[DateTime]]("updated_at", O.Nullable)
  
  override def * = (jobTitleId.?, companyId, name, code, description, createdUserId, createdAt, updatedUserId, updatedAt) <> (JobTitle.tupled, JobTitle.unapply)
  
  // foreign keys and indexes
  def company = foreignKey("fk_job_title_on_company_id", companyId, TableQuery[Companies])(_.companyId)
  
  def uniqueCode = index("idx_job_title_on_code_unique", (companyId, code), unique = true)
  
  def createdBy = foreignKey("fk_job_title_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_job_title_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
