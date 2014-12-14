package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime
import enums.CompanyStatusType._

/**
 * Companies
 * 
 */

class Companies(tag: Tag) extends Table[Company](tag, "company") {

  def companyId = column[Long]("company_id", O.PrimaryKey, O.AutoInc)
  
  def name = column[String]("name")
  
  def status = column[CompanyStatusType]("status")
  
  def address = column[String]("address")
  
  def email = column[String]("email")
  
  def website = column[Option[String]]("website")
  
  def telephone = column[String]("telephone")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (companyId.?, name, status, address, email, website, telephone, createdUserId, createdAt, updatedUserId, updatedAt) <> (Company.tupled, Company.unapply)
  
  // foreign keys and indexes
  def uniqueName = index("idx_company_on_name_unique", name, unique = true)
  
  def createdBy = foreignKey("fk_company_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_company_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
