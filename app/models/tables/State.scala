package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._

class States(tag: Tag) extends Table[State](tag, "state") {

  def stateId = column[Long]("state_id", O.PrimaryKey, O.AutoInc)
  
  def code = column[String]("code")
  
  def name = column[String]("name")
  
  def countryId = column[Long]("country_id")
  
  override def * = (stateId.?, code, name, countryId) <> (State.tupled, State.unapply)
  
  // foreign keys and indexes
  def country = foreignKey("fk_on_states_country_id", countryId, TableQuery[Users])(_.userId)
  
  def uniqueCode = index("idx_state_on_code_unique", (countryId, code), unique = true)
}