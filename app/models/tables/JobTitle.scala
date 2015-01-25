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
  
  def companyId = column[Long]("company_id")
  
  def name = column[String]("name")
  
  def description = column[String]("description")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (jobTitleId.?, companyId, name, description, createdUserId, createdAt, updatedUserId, updatedAt) <> (JobTitle.tupled, JobTitle.unapply)
  
  // foreign keys and indexes
  def company = foreignKey("fk_job_title_on_company_id", companyId, TableQuery[Companies])(_.companyId)
  
  def uniqueName = index("idx_job_title_on_company_id_name", (companyId, name), unique = true)
  
  def createdBy = foreignKey("fk_job_title_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_job_title_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
