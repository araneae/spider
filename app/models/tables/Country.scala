package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._

class Countries(tag: Tag) extends Table[Country](tag, "country") {

  def countryId = column[Long]("country_id", O.PrimaryKey, O.AutoInc)
  
  def code = column[String]("code")
  
  def name = column[String]("name")
  
  def active = column[Boolean]("active")
  
  override def * = (countryId.?, code, name, active) <> (Country.tupled, Country.unapply)
  
  // foreign keys and indexes
  
  def uniqueCode = index("idx_country_on_code", code, unique = true)
}
 