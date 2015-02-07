package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import enums.ContactStatus._
import org.joda.time.DateTime
import enums._
import utils._

object ContactRepository {
  
  val contactQuery = TableQuery[Contacts]
  val userDocumentQuery = TableQuery[UserDocuments]
  val userDocumentFolderQuery = TableQuery[UserDocumentFolders]
  
  def create(contact: Contact) = {
    DB.withSession {
       implicit session: Session =>
         contactQuery.insert(contact)
    }
  }
  
  def find(userId: Long, friendId: Long): Option[Contact] = {
    DB.withSession {
      implicit session =>
        contactQuery.filter(s => s.userId === userId && s.friendId === friendId).firstOption
    }
  }
  
  def getAll(userId: Long): Seq[ContactDTO] = {
    DB.withSession {
      implicit session =>
        val q = for {
            c <- contactQuery.filter(_.userId === userId)
            u <- c.contact
            up <- u.userProfilePersonal
        } yield (c.friendId, up.physicalFile, u.firstName, u.lastName, c.status)
         
        q.list.map{case (friendId, optPhysicalFile, firstName, lastName, status)
                => {
                      val pictureUrl = getPictureUrl(optPhysicalFile)
                      ContactDTO(friendId, pictureUrl, firstName, lastName, status)
                  }
        }
    }
  }
  
  def findContact(userId: Long, friendId: Long): Option[ContactDTO] = {
    DB.withSession {
      implicit session =>
        val q = for {
            c <-contactQuery.filter(s => s.userId === userId && s.friendId === friendId)
            u <- c.contact
            up <- u.userProfilePersonal
        } yield (c.friendId, up.physicalFile, u.firstName, u.lastName, c.status)
         
        q.firstOption.map{case (friendId, optPhysicalFile, firstName, lastName, status) 
                => {
                      val pictureUrl = getPictureUrl(optPhysicalFile)
                      ContactDTO(friendId, pictureUrl, firstName, lastName, status)
                    }
                }
    }
  }
  
  def findByToken(token: String): Option[Contact] = {
    DB.withSession {
      implicit session =>
        contactQuery.filter(s => s.token === token).firstOption
    }
  }
  
  def updateToken(userId: Long, friendId: Long, token: String) = {
    DB.withSession {
       implicit session: Session =>
         val q = for { 
                     cu <- contactQuery.filter (u => u.userId === userId && u.friendId === friendId)
                 } yield (cu.token, cu.updatedUserId, cu.updatedAt)
                 
         q.update((Some(token), Some(userId), Some(new DateTime())))
    }
  }
  
  def updateStatus(userId: Long, friendId: Long, status: ContactStatus, token: Option[String]) = {
    DB.withSession {
       implicit session: Session =>
         val q = for { 
                     u <- contactQuery if u.userId === userId && u.friendId === friendId 
                  } yield (u.status, u.token, u.updatedUserId, u.updatedAt)
         q.update((status, token, Some(friendId), Some(new DateTime())))
    }
  }
  
  def delete(userId: Long, friendId: Long) = {
    DB.withSession {
       implicit session: Session =>
          contactQuery.filter( u => u.userId === userId && u.friendId === friendId).delete
    }
  }
  
  def findAllWithDocumentShareAttributes(userId: Long, documentId: Long): Seq[ContactWithDocument] = {
    DB.withSession {
      implicit session =>
        val q = for {
          (c, d) <- contactQuery leftJoin userDocumentQuery on ((c, d) =>
                                          c.friendId === d.userId &&
                                          d.documentId === documentId) if c.userId === userId
          u <- c.contact
        } yield (u, d.?)
        
        q.list.map{case (u, d) 
                   => {
                        val friendId = u.userId.get
                        val name = s"${u.firstName} ${u.lastName}"
                        d match {
                          case (userDpocumentId, usrId, documentId, ownershipType, canCopy, canShare, canView, important, star, isLimitedShare, createdUserId, createdAt, shareUntilEOD, updatedUserId, updatedAt) =>
                            ContactWithDocument(friendId, name, documentId, canShare, canCopy, canView, isLimitedShare, shareUntilEOD)
                          case _ =>
                            ContactWithDocument(friendId, name, None, None, None, None, None, None)
                        }
                   }
        }
    }
  }
  
  def findAllWithDocumentFolderShareAttributes(userId: Long, documentFolderId: Long): Seq[ContactWithDocumentFolder] = {
    DB.withSession {
      implicit session =>
        val q = for {
          (c, d) <- contactQuery leftJoin userDocumentFolderQuery on ((c, d) =>
                                          c.friendId === d.userId &&
                                          d.documentFolderId === documentFolderId) if (c.userId === userId) && (c.status === ContactStatus.CONNECTED)
          u <- c.contact
        } yield (u, d.?)
        
        q.list.map{case (u, d) 
                   => {
                        val friendId = u.userId.get
                        val name = s"${u.firstName} ${u.lastName}"
                        d match {
                          case (userDocumentFolderId, documentFolderId, usrId, ownershipType, canCopy, canShare, canView, isLimitedShare, shareUntilEOD, createdUserId, createdAt, updatedUserId, updatedAt) =>
                            val shared = userDocumentFolderId match {
                              case Some(id) => true
                              case None => false
                            }
                            ContactWithDocumentFolder(friendId, name, shared, canCopy, canShare, canView, isLimitedShare, shareUntilEOD)
                          case _ =>
                            ContactWithDocumentFolder(friendId, name, false, None, None, None, None, None)
                        }
                   }
        }
    }
  }
  
  def getPictureUrl(optPhysicalFile: Option[String]): String = {
    optPhysicalFile match {
                        case Some(physicalFile) => Configuration.userProfilePictureUrl(physicalFile)
                        case None => Configuration.defaultProfilePictureUrl
                    }
  }
}

