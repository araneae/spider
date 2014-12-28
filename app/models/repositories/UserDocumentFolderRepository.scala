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
  
  val userDocumentFolderQuery = TableQuery[UserDocumentFolders]
  val documentQuery = TableQuery[Documents]
  val userDocumentQuery = TableQuery[UserDocuments]
  
  def create(userDocumentFolder: UserDocumentFolder) = {
    DB.withSession {
       implicit session: Session =>
         userDocumentFolderQuery insert userDocumentFolder
    }
  }
  
  def find(userId: Long, documentFolderId: Long): Option[UserDocumentFolder] = { 
    DB.withSession {
       implicit session: Session =>
          userDocumentFolderQuery filter( d => d.userId === userId && d.documentFolderId === documentFolderId ) firstOption
    }
  }
  
  def findAll(userId: Long): Seq[UserDocumentFolder] = { 
    DB.withSession {
       implicit session: Session =>
          userDocumentFolderQuery filter(x => x.userId === userId) list
    }
  }
  
  def getAll(userId: Long): Seq[FolderDTO] = { 
    DB.withSession {
       implicit session: Session =>
         val q = for {
            uf <- userDocumentFolderQuery filter(x => x.userId === userId)
            f <- uf.documentFolder
         } yield(uf, f)
         
         q.list map { case(uf, f) => FolderDTO(uf, f) }
    }
  }
  
  def findAllByOwnershipType(userId: Long, ownershipType: OwnershipType): Seq[UserDocumentFolder] = { 
    DB.withSession {
       implicit session: Session =>
          userDocumentFolderQuery filter(ud => ud.userId === userId && ud.ownershipType === ownershipType) list
    }
  }
  
  def getAllByOwnershipType(userId: Long, ownershipType: OwnershipType): Seq[UserDocumentFolderDTO] = { 
    DB.withSession {
       implicit session: Session =>
          val q = userDocumentFolderQuery filter(ud => ud.userId === userId && ud.ownershipType === ownershipType) 
          q.list map {ud => UserDocumentFolderDTO(ud)}
    }
  }
  
  def findDefault(userId: Long): Option[UserDocumentFolder] = { 
    DB.withSession {
       implicit session: Session =>
         val q = for {
            ud <- userDocumentFolderQuery filter(d => d.userId === userId)
            ds <- ud.documentFolder if ud.ownershipType === OwnershipType.OWNED
            if ds.default === true
         } yield (ud)
         
         q.firstOption.map { ud => ud}
    }
  }
  
  def getAllDocumentsByDocumentFolderId(userId: Long, documentFolderId : Long): Seq[FolderDocumentDTO] = { 
    DB.withSession {
       implicit session: Session =>
          val q = for {
              df <- userDocumentFolderQuery filter (x => x.userId === userId && x.documentFolderId === documentFolderId)
              d  <- documentQuery filter (x => x.documentFolderId === df.documentFolderId)
              u  <- d.createdBy
          } 
          yield (df, d, u)
         
          q.list.map{case (df, d, u) 
                 => FolderDocumentDTO(df.documentFolderId, d.documentId.get, d.name, d.description, df.ownershipType, d.signature, df.canCopy, df.canShare, df.canView, u.firstName, d.createdAt)}
    }
  }
  
  def getSharedDocuments(userId: Long): Seq[SharedUserDocumentDTO] = { 
    DB.withSession {
       implicit session: Session =>
         val now = new DateTime()
         val q = for {
             db <- userDocumentFolderQuery filter (x => x.userId === userId && x.ownershipType === OwnershipType.SHARED)
             u <- db.createdBy
             d <- documentQuery filter (x => x.documentFolderId === db.documentFolderId)
             ud <- userDocumentQuery filter (x => x.userId === userId && x.documentId =!= d.documentId)
         } yield(d, db.canCopy, db.canShare, db.canView, u)
         
         val q2 = for {
              ud <- userDocumentQuery filter(x => x.userId === userId && x.ownershipType === OwnershipType.SHARED)
              d  <- ud.document
              u  <- ud.createdBy
          } yield(d, ud.canCopy, ud.canShare, ud.canView, u)
         
         val unionQuery = q union q2
         unionQuery.list.map{case (d, canCopy, canShare, canView, u) =>
                             SharedUserDocumentDTO(d.documentFolderId, d.documentId.get, d.name, d.description, canCopy, canShare, canView, u.firstName)
                           }
    }
  }
  
  def udate(userDocumentFolder: UserDocumentFolder) = {
    DB.withSession {
       implicit session: Session =>
          userDocumentFolderQuery filter(d => d.userId === userDocumentFolder.userId && d.documentFolderId === userDocumentFolder.documentFolderId) update userDocumentFolder
    }
  }
  
  def delete(userDocumentFolderId: Long) = {
    DB.withSession {
       implicit session: Session =>
          userDocumentFolderQuery.filter(d => d.userDocumentFolderId === userDocumentFolderId) delete
    }
  }
  
}

