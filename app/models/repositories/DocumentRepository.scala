package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import enums.DocumentType._
import org.joda.time.DateTime

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
  
  def udateFolder(documentId: Long, documentFolderId: Long, userId: Long) = {
    DB.withSession {
       implicit session: Session =>
         val q = for {
            d <- query filter(_.documentId === documentId) 
         } yield(d.documentFolderId, d.updatedUserId, d.updatedAt)
         
         q update ((documentFolderId, Some(userId), Some(new DateTime)))
    }
  }
  
  def delete(documentId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter( u => u.documentId === documentId).delete
    }
  }
  
  def getDocumentCount(documentFolderId: Long): Int = {
    DB.withSession {
       implicit session: Session =>
          query.filter( u => u.documentFolderId === documentFolderId).list.length
    }
  }
}

