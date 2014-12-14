package models.repositories

import models.tables._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.dtos._
import org.joda.time.DateTime

object DomainRepository {
  
  val query = TableQuery[Domains]
  
  def create(domain: Domain) = {
    DB.withSession {
       implicit session: Session =>
         query returning query.map(_.domainId) += domain
    }
  }
  
  def update(domain: Domain, userId: Long) = {
    DB.withSession {
       implicit session: Session =>
         val q = for {
            d <- query.filter(_.domainId === domain.domainId)
          } yield (d.name, d.description, d.updatedUserId, d.updatedAt)
          
          q update((domain.name, domain.description, Some(userId), Some(new DateTime())))
    }
  }
  def find(domainId: Long): Option[DomainDTO] = {
    DB.withSession {
       implicit session: Session =>
          val q = for {
             s <- query filter (_.domainId === domainId)
             i <- s.industry
           } yield (s.domainId, s.industryId, s.name, s.description, i.name)
           
          val result = q.list.map{case (domainId, industryId, name, description, industryName) => 
                 DomainDTO(Some(domainId), industryId, name, description, industryName)}
           
          if (result.length > 0) Some(result(0))
          else None
    }
  }
  
  def findAll(): Seq[DomainDTO] = {
    DB.withSession {
       implicit session: Session =>
         val q = for {
             s <- query
             i <- s.industry
           } yield (s.domainId, s.industryId, s.name, s.description, i.name)
           
           q.list.map{case (domainId, industryId, name, description, industryName) => DomainDTO(Some(domainId), industryId, name, description, industryName)}
    }
  }
  
  def delete(domainId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter(_.domainId === domainId).delete
    }
  }
}

