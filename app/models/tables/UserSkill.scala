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
  
  def userId = column[Long]("user_id", O.NotNull)
  
  def skillId = column[Long]("skill_id", O.NotNull)
  
  def skillLevel = column[SkillLevel]("skill_level", O.NotNull)
  
  def descriptionShort = column[Option[String]]("description_short", O.Nullable)
  
  def descriptionLong = column[Option[String]]("description_long", O.Nullable)
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Option[Long]]("updated_user_id", O.Nullable)
  
  def updatedAt = column[Option[DateTime]]("updated_at", O.Nullable)
  
  override def * = (userId, skillId, skillLevel, descriptionShort, descriptionLong, createdUserId, createdAt, updatedUserId, updatedAt) <> (UserSkill.tupled, UserSkill.unapply)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_user_skill_user_id_skill_id", (userId, skillId))
  
  def user = foreignKey("fk_on_user_skill_user_id", userId, TableQuery[Users])(_.userId)
  
  def skill = foreignKey("fk_on_user_skill_skill_id", skillId, TableQuery[Skills])(_.skillId)
  
  def createdBy = foreignKey("fk_on_user_skill_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_user_skill_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
 