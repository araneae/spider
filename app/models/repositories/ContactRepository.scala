package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import enums.ContactStatus._
import org.joda.time.DateTime
import enums._

object ContactRepository {
  
  val query = TableQuery[Contacts]
  val userDocument = TableQuery[UserDocuments]
  val userDocumentBox = TableQuery[UserDocumentBoxes]
  
  def create(contact: Contact) = {
    DB.withSession {
       implicit session: Session =>
         query.insert(contact)
    }
  }
  
  def find(userId: Long, contactUserId: Long): Option[Contact] = {
    DB.withSession {
      implicit session =>
        query.filter(s => s.userId === userId && s.contactUserId === contactUserId).firstOption
    }
  }
  
  def findAll(userId: Long): Seq[ContactFull] = {
    DB.withSession {
      implicit session =>
        val q = for {
            c <- query.filter(_.userId === userId)
            a <- c.contact
        } yield (c.contactUserId, a.firstName, a.lastName, a.email, c.status)
         
        q.list.map{case (contactUserId, firstName, lastName, email, status) 
                => ContactFull(contactUserId, firstName, lastName, email, status)}
    }
  }
  
  def findContact(userId: Long, contactUserId: Long): Option[ContactFull] = {
    DB.withSession {
      implicit session =>
        val q = for {
            c <-query.filter(s => s.userId === userId && s.contactUserId === contactUserId)
            a <- c.contact
        } yield (c.contactUserId, a.firstName, a.lastName, a.email, c.status)
         
        val result = q.list.map{case (contactUserId, firstName, lastName, email, status) 
                => ContactFull(contactUserId, firstName, lastName, email, status)}
        if (result.length > 0) Some(result(0))
        else None
    }
  }
  
  def findByToken(token: String): Option[Contact] = {
    DB.withSession {
      implicit session =>
        query.filter(s => s.token === token).firstOption
    }
  }
  
  def updateToken(userId: Long, contactUserId: Long, token: String) = {
    DB.withSession {
       implicit session: Session =>
         val q = for { 
                     cu <- query.filter (u => u.userId === userId && u.contactUserId === contactUserId)
                 } yield (cu.token, cu.updatedUserId, cu.updatedAt)
                 
         q.update((Some(token), Some(userId), Some(new DateTime())))
    }
  }
  
  def updateStatus(userId: Long, contactUserId: Long, status: ContactStatus, token: Option[String]) = {
    DB.withSession {
       implicit session: Session =>
         val q = for { 
                     u <- query if u.userId === userId && u.contactUserId === contactUserId 
                  } yield (u.status, u.token, u.updatedUserId, u.updatedAt)
         q.update((status, token, Some(contactUserId), Some(new DateTime())))
    }
  }
  
  def delete(userId: Long, contactUserId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter( u => u.userId === userId && u.contactUserId === contactUserId).delete
    }
  }
  
  def findAllWithDocumentShareAttributes(userId: Long, documentId: Long): Seq[ContactWithDocument] = {
    DB.withSession {
      implicit session =>
        val q = for {
          (c, d) <- query leftJoin userDocument on ((c, d) =>
                                          c.contactUserId === d.userId &&
                                          d.documentId === documentId) if c.userId === userId
          cu <- c.contact
        } yield (cu.userId, cu.email, d.?)
        
        q.list.map{case (userId, email, d) 
                   => {
                        d match {
                          case (userDpocumentId, usrId, documentId, ownershipType, canCopy, canShare, canView, important, star, isLimitedShare, createdUserId, createdAt, shareUntilEOD, updatedUserId, updatedAt) =>
                            ContactWithDocument(userId, email, documentId, canShare, canCopy, canView, isLimitedShare, shareUntilEOD)
                          case _ =>
                            ContactWithDocument(userId, email, None, None, None, None, None, None)
                        }
                   }
        }
    }
  }
  
  def findAllWithDocumentBoxShareAttributes(userId: Long, documentBoxId: Long): Seq[ContactWithDocumentBox] = {
    DB.withSession {
      implicit session =>
        val q = for {
          (c, d) <- query leftJoin userDocumentBox on ((c, d) =>
                                          c.contactUserId === d.userId &&
                                          d.documentBoxId === documentBoxId) if c.userId === userId
          cu <- c.contact
        } yield (cu.userId, cu.email, d.?)
        
        q.list.map{case (userId, email, d) 
                   => {
                        d match {
                          case (userDocumentBoxId, documentBoxId, usrId, ownershipType, canCopy, isLimitedShare, shareUntilEOD, createdUserId, createdAt, updatedUserId, updatedAt) =>
                            val shared = userDocumentBoxId match {
                              case Some(id) => true
                              case None => false
                            }
                            ContactWithDocumentBox(userId, email, shared, canCopy, isLimitedShare, shareUntilEOD)
                          case _ =>
                            ContactWithDocumentBox(userId, email, false, None, None, None)
                        }
                   }
        }
    }
  }
}

