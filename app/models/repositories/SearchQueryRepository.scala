package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._

object SearchQueryRepository {
  
  val query = TableQuery[SearchQueries]
  
  def create(documentSearch: SearchQuery) = {
    DB.withSession {
       implicit session: Session =>
         query insert documentSearch
    }
  }
  
   def udate(searchQuery: SearchQuery) = {
    DB.withSession {
       implicit session: Session =>
         query.filter(_.searchQueryId === searchQuery.searchQueryId).update(searchQuery)
    }
  }
  
  def findAll(userId: Long): Seq[SearchQuery] = {
    DB.withSession {
      implicit session =>
       query.filter( d => d.userId === userId).list
    }
  }
  
  def delete(searchQueryId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter( d => d.searchQueryId === searchQueryId).delete
    }
  }
}

