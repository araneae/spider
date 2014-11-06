package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import enums.DocumentType._

object DocumentRepository {
  
  val query = TableQuery[Documents]
  
  def create(document: Document) = {
    DB.withSession {
       implicit session: Session =>
         query returning query.map(_.documentId) += document
    }
  }
  
  def find(documentId: Long): Option[Document] = {
    DB.withSession {
      implicit session =>
        query.filter(d => d.documentId === documentId).firstOption
    }
  }
  
  def udate(document: Document) = {
    DB.withSession {
       implicit session: Session =>
         query filter(_.documentId === document.documentId.get) update document 
    }
  }
  
  def delete(documentId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter( u => u.documentId === documentId).delete
    }
  }
}

