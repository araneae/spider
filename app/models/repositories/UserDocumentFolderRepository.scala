package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import enums.DocumentType._
import org.joda.time.DateTime
import enums._
import enums.OwnershipType._

object UserDocumentFolderRepository {
  
  val query = TableQuery[UserDocumentFolders]
  val documentQuery = TableQuery[Documents]
  
  def create(userDocumentFolder: UserDocumentFolder) = {
    DB.withSession {
       implicit session: Session =>
         query insert userDocumentFolder
    }
  }
  
  def find(userId: Long, documentFolderId: Long): Option[UserDocumentFolder] = { 
    DB.withSession {
       implicit session: Session =>
          query filter( d => d.userId === userId && d.documentFolderId === documentFolderId ) firstOption
    }
  }
  
  def findAll(userId: Long): Seq[UserDocumentFolder] = { 
    DB.withSession {
       implicit session: Session =>
          query filter(_.userId === userId) list
    }
  }
  
  def findAllByOwnershipType(userId: Long, ownershipType: OwnershipType): Seq[UserDocumentFolder] = { 
    DB.withSession {
       implicit session: Session =>
          query filter(ud => ud.userId === userId && ud.ownershipType === ownershipType) list
    }
  }
  
  def findDefault(userId: Long): Option[UserDocumentFolder] = { 
    DB.withSession {
       implicit session: Session =>
         val q = for {
            ud <- query filter(d => d.userId === userId)
            ds <- ud.documentFolder if ud.ownershipType === OwnershipType.OWNED
            if ds.default === true
         } yield (ud)
         
         val docStores = q.list
         if (docStores.length > 0)
           Some(docStores(0))
         else 
           None
    }
  }
  
  def findSharedDocuments(userId: Long): Seq[SharedUserDocumentDTO] = { 
    DB.withSession {
       implicit session: Session =>
         val now = new DateTime()
         val q = for {
             db <- query
             u <- db.createdBy
             d <- documentQuery
             if (db.documentFolderId === d.documentFolderId &&
                 db.ownershipType === OwnershipType.SHARED &&
                 db.userId === userId)
             //(db, d) <- query innerJoin documentQuery on ( _.documentFolderId === _.documentFolderId && _.userId === userId) 
         } yield(db, d, u)
         
         q.list.map { case (db, d, u) => 
             SharedUserDocumentDTO(db.documentFolderId, d.documentId.get, d.name, d.description, db.canCopy, u.firstName)
         }
    }
  }
  
  
  def udate(userDocumentFolder: UserDocumentFolder) = {
    DB.withSession {
       implicit session: Session =>
          query filter(d => d.userId === userDocumentFolder.userId && d.documentFolderId === userDocumentFolder.documentFolderId) update userDocumentFolder
    }
  }
  
  def delete(userDocumentFolderId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter(d => d.userDocumentFolderId === userDocumentFolderId) delete
    }
  }
  
}

