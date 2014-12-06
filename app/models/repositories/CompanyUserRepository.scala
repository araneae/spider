package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import enums.DocumentType._
import org.joda.time.DateTime

object CompanyUserRepository {
  
  val query = TableQuery[CompanyUsers]
  
  def create(companyUser: CompanyUser) = {
    DB.withSession {
       implicit session: Session =>
         query insert companyUser
    }
  }
  
  def findAll(companyId: Long): Seq[CompanyUser] = {
    DB.withSession {
       implicit session: Session =>
          val q = for {
              cu <- query.filter(_.companyId === companyId)
          } 
          yield (cu)
         
          q.list
    }
  }

  def delete(companyUserId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter(cu => cu.companyId === companyUserId) delete
    }
  }
}

