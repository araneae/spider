package models.repositories

import models.tables._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.dtos._

object IndustryRepository {
  
  val query = TableQuery[Industries]
  
  def create(industry: Industry) : Long = {
    DB.withSession {
       implicit session: Session =>
         query returning query.map(_.id) += industry
         
    }
  }
  
  def udate(industry: Industry) = {
    DB.withSession {
       implicit session: Session =>
         query filter(_.id === industry.id) update industry 
    }
  }

  def find(id: Long): Option[Industry] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.id === id) firstOption
    }
  }
  
  def findByCode(code: String): Option[Industry] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.code === code) firstOption
    }
  }
  
  def findAll(): Seq[Industry] = {
    DB.withSession {
       implicit session: Session =>
          query list
    }
  }
  
  def delete(id: Long) = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.id === id) delete
    }
  }
}

