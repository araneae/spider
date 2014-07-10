package models.tables

import play.api.db.slick.Config.driver.simple._

/**
 * This class presents a configuration parameter and it's value. 
 */

case class Config(name: String,
                  value: String,
                  description: String)

class Configs(tag: Tag) extends Table[Config](tag, "config") {

  def name = column[String]("name", O.PrimaryKey)
  
  def value = column[String]("value", O.NotNull)
  
  def description = column[String]("description", O.Nullable)
  
  override def * = (name, value, description) <> (Config.tupled, Config.unapply)
  
  // foreign keys and indexes
}