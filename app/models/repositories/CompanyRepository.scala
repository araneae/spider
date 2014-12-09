package models.repositories

import models.tables._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.dtos._
import org.joda.time.DateTime

object CompanyRepository {
  
  val query = TableQuery[Companies]
  
  def find(companyId: Long): Option[Company] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.companyId === companyId) firstOption
    }
  }
  
  def create(company: Company) :Long = {
    DB.withSession {
      implicit session =>
        query returning query.map(_.companyId) += company
    }
  }
  
  def findByUserId(userId: Long): Option[Company] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.createdUserId === userId) firstOption
    }
  }
  
  def update(company: Company) = {
    DB.withSession {
      implicit session =>
        query filter(_.companyId === company.companyId) update company
    }
  }
}