package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

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
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Long]("updated_user_id", O.Nullable)
  
  def updatedAt = column[DateTime]("updated_at", O.Nullable)
  
  override def * = (skillId.?, industryId, name, code, description, createdUserId, createdAt, updatedUserId.?, updatedAt.?) <> (Skill.tupled, Skill.unapply)
  
  // foreign keys and indexes
  def industry = foreignKey("fk_skill_on_industry_id", industryId, TableQuery[Industries])(_.industryId)
  
  def uniqueCode = index("idx_skill_on_code_unique", code, unique = true)
  
  def createdBy = foreignKey("fk_on_skill_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_skill_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
