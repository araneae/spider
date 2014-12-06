package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import enums.DocumentType._
import org.joda.time.DateTime
import enums.OwnershipType

object UserDocumentBoxRepository {
  
  val query = TableQuery[UserDocumentBoxes]
  val documentQuery = TableQuery[Documents]
  
  def create(userDocumentBox: UserDocumentBox) = {
    DB.withSession {
       implicit session: Session =>
         query insert userDocumentBox
    }
  }
  
  def find(userId: Long, documentBoxId: Long): Option[UserDocumentBox] = { 
    DB.withSession {
       implicit session: Session =>
          query filter( d => d.userId === userId && d.documentBoxId === documentBoxId ) firstOption
    }
  }
  
  def findAll(userId: Long): Seq[UserDocumentBox] = { 
    DB.withSession {
       implicit session: Session =>
          query filter(_.userId === userId) list
    }
  }
  
  def findDefault(userId: Long): Option[UserDocumentBox] = { 
    DB.withSession {
       implicit session: Session =>
         val q = for {
            ud <- query filter(d => d.userId === userId)
            ds <- ud.documentBox if ud.ownershipType === OwnershipType.OWNED
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
             if (db.documentBoxId === d.documentBoxId &&
                 db.ownershipType === OwnershipType.SHARED &&
                 db.userId === userId)
             //(db, d) <- query innerJoin documentQuery on ( _.documentBoxId === _.documentBoxId && _.userId === userId) 
         } yield(db, d, u)
         
         q.list.map { case (db, d, u) => 
             SharedUserDocumentDTO(db.documentBoxId, d.documentId.get, d.name, d.description, db.canCopy, u.firstName)
         }
    }
  }
  
  
  def udate(userDocumentBox: UserDocumentBox) = {
    DB.withSession {
       implicit session: Session =>
          query filter(d => d.userId === userDocumentBox.userId && d.documentBoxId === userDocumentBox.documentBoxId) update userDocumentBox
    }
  }
  
  def delete(userDocumentBoxId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter(d => d.userDocumentBoxId === userDocumentBoxId) delete
    }
  }
  
}

