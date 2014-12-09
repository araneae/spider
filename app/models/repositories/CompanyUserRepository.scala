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
  
  def find(companyUserId: Long): Option[CompanyUser] = {
    DB.withSession {
       implicit session: Session =>
         query filter(_.companyUserId === companyUserId) firstOption
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

  def update(companyUser: CompanyUser) = {
    DB.withSession {
       implicit session: Session =>
         query filter(_.companyUserId === companyUser.companyUserId) update companyUser
    }
  }
  
  def delete(companyUserId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter(cu => cu.companyId === companyUserId) delete
    }
  }
}

