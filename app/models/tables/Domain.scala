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
  
  def industryId = column[Long]("industry_id", O.NotNull)
  
  def name = column[String]("name", O.NotNull)
  
  def code = column[String]("code", O.NotNull)
  
  def description = column[Option[String]]("description", O.Nullable)
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Option[Long]]("updated_user_id", O.Nullable)
  
  def updatedAt = column[Option[DateTime]]("updated_at", O.Nullable)
  
  override def * = (domainId.?, industryId, name, code, description, createdUserId, createdAt, updatedUserId, updatedAt) <> (Domain.tupled, Domain.unapply)
  
    // foreign keys and indexes
  def industry = foreignKey("fk_on_skill_industry_id", industryId, TableQuery[Industries])(_.industryId)
  
  def uniqueCode = index("idx_unique_on_skill_code", code, unique = true)
  
  def createdBy = foreignKey("fk_on_domain_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_domain_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
