package models.repositories

import models.tables._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.dtos._

object CountryRepository {
  
  val query = TableQuery[Countries]
  
  def find(countryId: Long): Option[Country] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.countryId === countryId) firstOption
    }
  }
  
  def findByCode(code: String): Option[Country] = {
    DB.withSession {
       implicit session: Session =>
          query filter(c => c.code === code) firstOption
    }
  }
  
  def findByName(name: String): Option[Country] = {
    DB.withSession {
       implicit session: Session =>
          query filter(c => c.name === name) firstOption
    }
  }
  
  def create(country: Country): Long = {
    DB.withSession {
      implicit session =>
        query returning query.map(_.countryId) += country
    }
  }
  
  def findAll(): Seq[Country] = {
    DB.withSession {
      implicit session =>
        query list
    }
  }
  
  def findAllAvailable(): Seq[Country] = {
    DB.withSession {
      implicit session =>
        query filter (_.active === true) list
    }
  }
}

