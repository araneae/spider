package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

/**
 * Industry signifies the highest level of expertise, like "Software", "Construction", Hospitality", "Medical" etc.
 * 
 */

class Industries(tag: Tag) extends Table[Industry](tag, "industry") {

  def industryId = column[Long]("industry_id", O.PrimaryKey, O.AutoInc)
  
  def code = column[String]("code", O.NotNull)
  
  def name = column[String]("name", O.NotNull)
  
  def description = column[Option[String]]("description", O.Nullable)
  
  def createdUserId = column[Long]("created_user_id", O.NotNull)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedUserId = column[Option[Long]]("updated_user_id", O.Nullable)
  
  def updatedAt = column[Option[DateTime]]("updated_at", O.Nullable)
  
  override def * = (industryId.?, code, name, description, createdUserId, createdAt, updatedUserId, updatedAt) <> (Industry.tupled, Industry.unapply)
  
  // foreign keys and indexes
  def uniqueCode = index("idx_unique_on_industry_code", code, unique = true)
  
  def createdBy = foreignKey("fk_on_industry_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_industry_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
