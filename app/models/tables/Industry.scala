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
  
  def name = column[String]("name")
  
  def description = column[String]("description")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (industryId.?, name, description, createdUserId, createdAt, updatedUserId, updatedAt) <> (Industry.tupled, Industry.unapply)
  
  // foreign keys and indexes
  def uniqueName = index("idx_industry_on_name", name, unique = true)
  
  def createdBy = foreignKey("fk_industry_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_industry_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
