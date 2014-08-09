package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._

object DocumentSearchRepository {
  
  val query = TableQuery[DocumentSearches]
  
  def create(documentSearch: DocumentSearch) = {
    DB.withSession {
       implicit session: Session =>
         query insert documentSearch
    }
  }
  
   def udate(documentSearch: DocumentSearch) = {
    DB.withSession {
       implicit session: Session =>
         query.filter(_.documentSearchId === documentSearch.documentSearchId).update(documentSearch)
    }
  }
  
  def findAll(userId: Long): Seq[DocumentSearch] = {
    DB.withSession {
      implicit session =>
       query.filter( d => d.userId === userId).list
    }
  }
  
  def delete(documentSearchId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter( d => d.documentSearchId === documentSearchId).delete
    }
  }
}

