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
  
  def industryId = column[Long]("industry_id")
  
  def name = column[String]("name")
  
  def description = column[String]("description")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (skillId.?, industryId, name, description, createdUserId, createdAt, updatedUserId, updatedAt) <> (Skill.tupled, Skill.unapply)
  
  // foreign keys and indexes
  def industry = foreignKey("fk_skill_on_industry_id", industryId, TableQuery[Industries])(_.industryId)
  
  def uniqueName = index("idx_skill_on_name_unique", name, unique = true)
  
  def createdBy = foreignKey("fk_on_skill_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_skill_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
