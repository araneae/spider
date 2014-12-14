package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import enums.DocumentType._
import org.joda.time.DateTime

object UserDocumentRepository {
  
  val query = TableQuery[UserDocuments]
  
  def create(userDocument: UserDocument) = {
    DB.withSession {
       implicit session: Session =>
         query insert userDocument
    }
  }
  
  def findAll(userId: Long): Seq[UserDocumentDTO] = { 
    DB.withSession {
       implicit session: Session =>
          val q = for {
              ud <- query.filter(_.userId === userId)
              d  <- ud.document
              u  <- ud.createdBy
          } 
          yield (ud.userDocumentId, ud.documentId, d.name, d.description, false, ud.ownershipType, d.signature, ud.canCopy, ud.canShare, ud.canView, u.firstName, ud.createdAt)
         
          q.sortBy(_._12.desc).list.map{case (userDocumentId, documentId, name, description, connected, ownershipType, signature, canCopy, canShare, canView, createdBy, createdAt) 
                 => UserDocumentDTO(userDocumentId, documentId, name, description, connected, ownershipType, signature, canCopy, canShare, canView, createdBy, createdAt)}
    }
  }
  
  def findAllByDocumentIds(userId: Long, documentIds : Seq[Long]): Seq[UserDocumentDTO] = { 
    DB.withSession {
       implicit session: Session =>
          val q = for {
              ud <- query.filter(d => d.userId === userId && (d.documentId inSet documentIds))
              d  <- ud.document
              u  <- ud.createdBy
          } 
          yield (ud.userDocumentId, ud.documentId, d.name, d.description, false, ud.ownershipType, d.signature, ud.canCopy, ud.canShare, ud.canView, u.firstName, ud.createdAt)
         
          q.list.map{case (userDocumentId, documentId, name, description, connected, ownershipType, signature, canCopy, canShare, canView, createdBy, createdAt) 
                 => UserDocumentDTO(userDocumentId, documentId, name, description, connected, ownershipType, signature, canCopy, canShare, canView, createdBy, createdAt)}
    }
  }
  
  def findAllByDocumentIds(documentIds : Seq[Long]): Seq[UserDocumentDTO] = { 
    DB.withSession {
       implicit session: Session =>
          val q = for {
              ud <- query.filter(d => d.documentId inSet documentIds)
              d  <- ud.document
              u  <- ud.createdBy
          } 
          yield (ud.userDocumentId, ud.documentId, d.name, d.description, false, ud.ownershipType, d.signature, ud.canCopy, ud.canShare, ud.canView, u.firstName, ud.createdAt)
         
          q.list.map{case (userDocumentId, documentId, name, description, connected, ownershipType, signature, canCopy, canShare, canView, createdBy, createdAt) 
                 => UserDocumentDTO(userDocumentId, documentId, name, description, connected, ownershipType, signature, canCopy, canShare, canView, createdBy, createdAt)}
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
          query filter(d => d.userId === userDocument.userId && d.documentId === userDocument.documentId) update userDocument
    }
  }
  
  def delete(userId: Long, documentId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter(d => d.userId === userId && d.documentId === documentId) delete
    }
  }
  
  def findByFileName(userId: Long, fileName : String): Seq[UserDocument] = { 
    DB.withSession {
       implicit session: Session =>
          val q = for {
              ud <- query.filter(d => d.userId === userId)
              d  <- ud.document if d.fileName === fileName
          } 
          yield (ud)
         
         q.list
    }
  }
  
  def findByName(userId: Long, name : String): Seq[UserDocument] = { 
    DB.withSession {
       implicit session: Session =>
          val q = for {
              ud <- query.filter(d => d.userId === userId)
              d  <- ud.document if d.name === name
          } 
          yield (ud)
         
         q.list
    }
  }
  
  def getCopyFileName(userId: Long, fileName: String) : String = {
     def getNextName(seq: Int) : String = {
         val copyName = s"${fileName}_copy${seq}"
         val docs = findByFileName(userId, copyName)
         if (docs.length > 0) {
               getNextName(seq + 1)
         } else {
            copyName
         }
      }
     getNextName(1)
  }
  
  def getCopyName(userId: Long, name: String) : String = {
     def getNextName(seq: Int) : String = {
         val copyName = s"${name}_copy${seq}"
         val docs = findByName(userId, copyName)
         if (docs.length > 0) {
               getNextName(seq + 1)
         } else {
            copyName
         }
      }
     getNextName(1)
  }
}

