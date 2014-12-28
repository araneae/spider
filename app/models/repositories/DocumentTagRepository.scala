package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._

object DocumentTagRepository {
  
  val query = TableQuery[DocumentTags]
  
  def create(documentTag: DocumentTag) = {
    DB.withSession {
       implicit session: Session =>
         query insert documentTag
    }
  }
  
  def findAll(userId: Long, documentId: Long): Seq[DocumentTag] = {
    DB.withSession {
      implicit session =>
       query.filter( d => d.userId === userId &&
                          d.documentId === documentId).list
    }
  }
  
  def getAll(userId: Long, documentId: Long): Seq[DocumentTagDTO] = {
    DB.withSession {
      implicit session =>
       query.filter( d => d.userId === userId &&
                          d.documentId === documentId).list.map{ dt => DocumentTagDTO(dt) }
    }
  }
  
  def findByUserTagId(userId: Long, userTagId: Long): Seq[DocumentTag] = {
    DB.withSession {
      implicit session =>
       query.filter( d => d.userId === userId &&
                          d.userTagId === userTagId).list
    }
  }
  
  def findByUserTagIdDocumentId( userTagId: Long, documentId: Long): Option[DocumentTag] = {
    DB.withSession {
      implicit session =>
       query.filter( d => d.userTagId === userTagId && d.documentId === documentId) firstOption
    }
  }
  
  def findDocumentByUserTagId(userId: Long, userTagId: Long): Seq[UserDocumentDTO] = {
    DB.withSession {
      implicit session =>
       val q = for {
            docTag <- query.filter( d => d.userId === userId && d.userTagId === userTagId)
            ud <- docTag.userDocument
            doc <- ud.document
            u  <- ud.createdBy
        } yield (ud.userDocumentId.?, ud.documentId, doc.name, doc.description, true, ud.ownershipType, doc.signature, ud.canCopy, ud.canShare, ud.canView, u.firstName, ud.createdAt)
        
        q.sortBy(_._12.desc).list.map{case (userDocumentId, documentId, name, description, connected, ownershipType, signature, canCopy, canShare, canView, createdBy, createdAt) 
                 => UserDocumentDTO(userDocumentId, documentId, name, description, connected, ownershipType, signature, canCopy, canShare, canView, createdBy, createdAt)}
    }
  }
  
  def findDocumentByUserTagIdAndDocumentIds(userId: Long, userTagId: Long, documentIds : Seq[Long]): Seq[UserDocumentDTO] = {
    DB.withSession {
      implicit session =>
       val q = for {
            docTag <- query.filter( d => (d.userId === userId) && (d.userTagId === userTagId) && (d.documentId inSet documentIds))
            ud <- docTag.userDocument
            doc <- ud.document
            u  <- ud.createdBy
        } yield (ud.userDocumentId.?, ud.documentId, doc.name, doc.description, true, ud.ownershipType, doc.signature, ud.canCopy, ud.canShare, ud.canView, u.firstName, ud.createdAt)
        
        q.sortBy(_._12.desc).list.map{case (userDocumentId, documentId, name, description, connected, ownershipType, signature, canCopy, canShare, canView, createdBy, createdAt) 
                 => UserDocumentDTO(userDocumentId, documentId, name, description, connected, ownershipType, signature, canCopy, canShare, canView, createdBy, createdAt)}
    }
  }
  
  def delete(userId: Long, documentId: Long, userTagId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter( d => d.userId === userId &&
                             d.userTagId === userTagId &&
                             d.documentId === documentId).delete
    }
  }
  
  def deleteByUserTagId(userId: Long, userTagId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter( d => d.userId === userId &&
                             d.userTagId === userTagId).delete
    }
  }
  
  def deleteByDocumentId(userId: Long, documentId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter( d => d.userId === userId &&
                             d.documentId === documentId).delete
    }
  }
}

