package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._

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
  
  def description = column[String]("description", O.Nullable)
  
  override def * = (domainId.?, industryId, name, code, description) <> (Domain.tupled, Domain.unapply)
  
    // foreign keys and indexes
  def industry = foreignKey("fk_on_skill_industry_id", industryId, TableQuery[Industries])(_.industryId)
  
  def uniqueCode = index("idx_unique_on_skill_code", code, unique = true)
}
