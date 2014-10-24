package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import enums.ContactStatus._
import org.joda.time.DateTime

object ContactRepository {
  
  val query = TableQuery[Contacts]
  val sharedDocument = TableQuery[SharedDocuments]
  
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
        } yield (c.contactUserId, a.firstName, a.lastName, a.email)
         
        q.list.map{case (contactUserId, firstName, lastName, email) 
                => ContactFull(contactUserId, firstName, lastName, email, true)}
    }
  }
  
  def findContact(userId: Long, contactUserId: Long): Seq[ContactFull] = {
    DB.withSession {
      implicit session =>
        val q = for {
            c <-query.filter(s => s.userId === userId && s.contactUserId === contactUserId)
            a <- c.contact
        } yield (c.contactUserId, a.firstName, a.lastName, a.email)
         
        q.list.map{case (contactUserId, firstName, lastName, email) 
                => ContactFull(contactUserId, firstName, lastName, email, true)}
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
                 
         q.update((token, userId, new DateTime()))
    }
  }
  
  def updateStatus(userId: Long, contactUserId: Long, status: ContactStatus, token: Option[String]) = {
    DB.withSession {
       implicit session: Session =>
         val q = for { 
                     u <- query if u.userId === userId && u.contactUserId === contactUserId 
                  } yield (u.status, u.token, u.updatedUserId, u.updatedAt)
         q.update((status, token.getOrElse(null), contactUserId, new DateTime()))
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
          (c, d) <- query leftJoin sharedDocument on ((c, d) =>
                                          c.contactUserId === d.userId &&
                                          c.userId === d.sharedByUserId &&
                                          d.documentId === documentId) if c.userId === userId
          cu <- c.contact
        } yield (cu.userId, cu.email, d.documentId.?, d.canShare.?, d.canCopy.?)
        
        q.list.map{case (userId, email, docId, canShare, canCopy) 
                => {  val shared = docId match { case Some(d) => true
                                                 case None => false
                                               }
                      ContactWithDocument(userId, email, documentId, shared, canShare, canCopy)
                   }
        }
    }
  }
}

