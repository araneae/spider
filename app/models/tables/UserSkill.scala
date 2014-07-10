package models.tables

import play.api.db.slick.Config.driver.simple._
import enums.SkillLevel._
import enums._
import models.dtos._

/**
 * Longersection table between user and skill tables.
 * 
 */

class UserSkills(tag: Tag) extends Table[UserSkill](tag, "user_skill") {
  
  def userId = column[Long]("user_id", O.NotNull)
  
  def skillId = column[Long]("skill_id", O.NotNull)
  
  def skillLevel = column[SkillLevel]("skill_level", O.NotNull)
  
  def descriptionShort = column[String]("description_short", O.Nullable)
  
  def descriptionLong = column[String]("description_long", O.Nullable)
  
  override def * = (userId, skillId, skillLevel, descriptionShort, descriptionLong) <> (UserSkill.tupled, UserSkill.unapply)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_user_skill_user_id_skill_id", (userId, skillId))
  
  def user = foreignKey("fk_on_user_skill_user_id", userId, TableQuery[Users])(_.id)
  
  def skill = foreignKey("fk_on_user_skill_skill_id", skillId, TableQuery[Skills])(_.id)
}
 