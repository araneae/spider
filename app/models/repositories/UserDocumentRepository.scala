package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import enums.DocumentType._

object UserDocumentRepository {
  
  val query = TableQuery[UserDocuments]
  
  def create(userDocument: UserDocument) = {
    DB.withSession {
       implicit session: Session =>
         query insert userDocument
    }
  }
  
  def findAll(userId: Long): Seq[UserDocumentFull] = { 
    DB.withSession {
       implicit session: Session =>
          val q = for {
              ud <- query.filter(_.userId === userId)
              d  <- ud.document
              u  <- ud.createdBy
          } 
          yield (ud.documentId, d.name, d.description, false, ud.ownershipType, d.signature, ud.canCopy, ud.canShare, u.firstName, ud.createdAt)
         
          q.list.map{case (documentId, name, description, connected, ownershipType, signature, canCopy, canShare, createdBy, createdAt) 
                 => UserDocumentFull(documentId, name, description, connected, ownershipType, signature, canCopy, canShare, createdBy, createdAt)}
    }
  }
  
  def findAllByDocumentIds(userId: Long, documentIds : Seq[Long]): Seq[UserDocumentFull] = { 
    DB.withSession {
       implicit session: Session =>
          val q = for {
              ud <- query.filter(d => d.userId === userId && (d.documentId inSet documentIds))
              d  <- ud.document
              u  <- ud.createdBy
          } 
          yield (ud.documentId, d.name, d.description, false, ud.ownershipType, d.signature, ud.canCopy, ud.canShare, u.firstName, ud.createdAt)
         
          q.list.map{case (documentId, name, description, connected, ownershipType, signature, canCopy, canShare, createdBy, createdAt) 
                 => UserDocumentFull(documentId, name, description, connected, ownershipType, signature, canCopy, canShare, createdBy, createdAt)}
    }
  }
  
  def findAllByDocumentId(documentId: Long): Seq[UserDocument] = {
    DB.withSession {
       implicit session: Session =>
          query.filter(d => d.documentId === documentId) list
    }
  }
  
  def findDocumentIds(userId: Long): Seq[Long] = {
    DB.withSession {
       implicit session: Session =>
          val q = for {
              ud <- query.filter(d => d.userId === userId)
          }
          yield (ud.documentId)
          
          q.list
    }
  }
  
  def find(userId: Long, documentId: Long): Option[UserDocument] = {
    DB.withSession {
       implicit session: Session =>
          query.filter(d => d.userId === userId && d.documentId === documentId) firstOption
    }
  }
  
  def udate(userDocument: UserDocument) = {
    DB.withSession {
       implicit session: Session =>
         query filter(d => d.userId === userDocument.userId && d.documentId === userDocument.documentId ) update userDocument 
    }
  }
  
  def delete(userId: Long, documentId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter(d => d.userId === userId && d.documentId === documentId) delete
    }
  }
}

