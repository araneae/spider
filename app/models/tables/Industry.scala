package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._

/**
 * Industry signifies the highest level of expertise, like "Software", "Construction", Hospitality", "Medical" etc.
 * 
 */

class Industries(tag: Tag) extends Table[Industry](tag, "industry") {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  
  def code = column[String]("code", O.NotNull)
  
  def name = column[String]("name", O.NotNull)
  
  def description = column[String]("description", O.Nullable)
  
  override def * = (id.?, code, name, description) <> (Industry.tupled, Industry.unapply)
  
  // foreign keys and indexes
  def uniqueCode = index("idx_unique_on_industry_code", code, unique = true)
}
