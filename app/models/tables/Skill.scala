package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._

/**
 * An user can have multiple skills, like Java, C++, ASP.NET, Scala,
 * etc. in Software Industry. Similarly "Carpentry", "Wielding", "Plumbing" etc. in Construction Industry.
 * An skill will fall under an Industry.
 */

class Skills(tag: Tag) extends Table[Skill](tag, "skill") {

  def skillId = column[Long]("skill_id", O.PrimaryKey, O.AutoInc)
  
  def industryId = column[Long]("industry_id", O.NotNull)
  
  def name = column[String]("name", O.NotNull)
  
  def code = column[String]("code", O.NotNull)
  
  def description = column[String]("description", O.Nullable)
  
  override def * = (skillId.?, industryId, name, code, description) <> (Skill.tupled, Skill.unapply)
  
  // foreign keys and indexes
  def industry = foreignKey("fk_skill_on_industry_id", industryId, TableQuery[Industries])(_.industryId)
  
  def uniqueCode = index("idx_skill_on_code_unique", code, unique = true)
}
