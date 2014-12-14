package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

/**
 * A domain is a somewhat broader definition of expertise area, like "Telecom", "Retail", "Insurance"
 * etc. in Software Industry. An user can one or more or none experience in domains. A domain will fall
 * under an Industry.
 */

class Domains(tag: Tag) extends Table[Domain](tag, "domain") {

  def domainId = column[Long]("domain_id", O.PrimaryKey, O.AutoInc)
  
  def industryId = column[Long]("industry_id")
  
  def name = column[String]("name")
  
  def description = column[String]("description")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (domainId.?, industryId, name, description, createdUserId, createdAt, updatedUserId, updatedAt) <> (Domain.tupled, Domain.unapply)
  
    // foreign keys and indexes
  def industry = foreignKey("fk_on_domain_industry_id", industryId, TableQuery[Industries])(_.industryId)
  
  def uniqueName = index("idx_unique_on_skill_name", name, unique = true)
  
  def createdBy = foreignKey("fk_on_domain_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_domain_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
