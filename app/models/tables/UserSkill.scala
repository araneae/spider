package models.tables

import play.api.db.slick.Config.driver.simple._
import enums.SkillLevel._
import enums._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

/**
 * Longersection table between user and skill tables.
 * 
 */

class UserSkills(tag: Tag) extends Table[UserSkill](tag, "user_skill") {
  
  def userId = column[Long]("user_id")
  
  def skillId = column[Long]("skill_id")
  
  def skillLevel = column[SkillLevel]("skill_level")
  
  def descriptionShort = column[Option[String]]("description_short")
  
  def descriptionLong = column[Option[String]]("description_long")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (userId, skillId, skillLevel, descriptionShort, descriptionLong, createdUserId, createdAt, updatedUserId, updatedAt) <> (UserSkill.tupled, UserSkill.unapply)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_user_skill", (userId, skillId))
  
  def user = foreignKey("fk_user_skill_on_user_id", userId, TableQuery[Users])(_.userId)
  
  def skill = foreignKey("fk_user_skill_on_skill_id", skillId, TableQuery[Skills])(_.skillId)
  
  def createdBy = foreignKey("fk_user_skill_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_user_skill_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
 