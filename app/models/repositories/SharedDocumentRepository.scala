package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import enums.DocumentType._

object SharedDocumentRepository {
  
  val query = TableQuery[SharedDocuments]
  
  def create(sharedDocument: SharedDocument) = {
    DB.withSession {
       implicit session: Session =>
         query insert sharedDocument
    }
  }
  
  def findAll(userId: Long): Seq[SharedDocumentFull] = {
    DB.withSession {
       implicit session: Session =>
          val q = for {
              sd <- query.filter(_.userId === userId)
              d  <- sd.document
              u  <- sd.sharedBy
          } 
          yield (sd.documentId, d.name, u.firstName, sd.canCopy)
         
          q.list.map{case (documentId, name, sharedBy, canCopy) 
                 => SharedDocumentFull(documentId, name, sharedBy, canCopy)}
    }
  }
  
  def udate(sharedDocument: SharedDocument) = {
    DB.withSession {
       implicit session: Session =>
         query filter(d => d.userId === sharedDocument.userId && d.documentId === sharedDocument.documentId ) update sharedDocument 
    }
  }
  
  def delete(userId: Long, documentId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter(d => d.userId === userId && d.documentId === documentId).delete
    }
  }
}

