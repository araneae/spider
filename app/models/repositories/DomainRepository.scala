package models.repositories

import models.tables._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.dtos._

object DomainRepository {
  
  val query = TableQuery[Domains]
  
  def create(domain: Domain) = {
    DB.withSession {
       implicit session: Session =>
         query returning query.map(_.id) += domain
    }
  }
  
  def udate(domain: Domain) = {
    DB.withSession {
       implicit session: Session =>
         query.filter(_.id === domain.id).update(domain)
    }
  }
  def find(id: Long): Option[Domain] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.id === id) firstOption
    }
  }
  
  def findByCode(code: String): Option[Domain] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.code === code) firstOption
    }
  }
  
  def findAll(): Seq[DomainFull] = {
    DB.withSession {
       implicit session: Session =>
         val q = for {
             s <- query
             i <- s.industry
           } yield (s.id, s.industryId, s.name, s.code, s.description, i.name)
           
           q.list.map{case (id, industryId, name, code, description, industryName) => DomainFull(id, industryId, name, code, description, industryName)}
    }
  }
  
  def delete(id: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter(_.id === id).delete
    }
  }
}

