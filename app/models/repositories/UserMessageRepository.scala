package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import org.joda.time.DateTime
import models.tables._
import models.dtos._

object UserMessageRepository {
  
  val query = TableQuery[UserMessages]
  
  def create(userMessage: UserMessage) = {
    DB.withSession {
       implicit session: Session =>
         query.insert(userMessage)
    }
  }
  
  def findAll(userId: Long): Seq[UserMessageFull] = {
    DB.withSession {
      implicit session =>
        val q = for {
            um <- query.filter(a => a.userId ===  userId)
            m <- um.message
            s <- m.sender
            mb <- um.messageBox
         } yield (um.messageId, um.messageBoxId, mb.messageBoxType, m.subject, m.body, s.firstName, um.read, um.replied, um.important, um.star, um.createdAt)
        
         q.list.map{case (messageId, messageBoxId, messageBoxType, subject, body, firstName, read, replied, important, star, createdAt) 
                => UserMessageFull(messageId, messageBoxId, messageBoxType, subject, body, firstName, read, replied, important, star, createdAt)}
    }
  }
  
  def find(userId: Long, messageId: Long): Option[UserMessage] = {
    DB.withSession {
      implicit session =>
        query filter(u => u.userId === userId && u.messageId === messageId) firstOption
    }
  }
  
  def udate(userMessage: UserMessage) = {
    DB.withSession {
       implicit session: Session =>
         query filter(m => m.userId === userMessage.userId && m.messageId === userMessage.messageId) update userMessage 
    }
  }
  
   def delete(messageId: Long, userId: Long) = {
    DB.withSession {
       implicit session: Session =>
         query filter(m => m.userId === userId && m.messageId === messageId) delete 
    }
  }
  
  def markStar(messageId: Long, userId: Long) = {
    DB.withSession {
       implicit session: Session =>
         val q = for { 
           um <-  query filter(m => m.userId === userId && m.messageId === messageId)
         } yield (um.star, um.updatedUserId, um.updatedAt)
         
         q.update((true, userId, new DateTime())) 
    }
  }
  
  def removeStar(messageId: Long, userId: Long) = {
    DB.withSession {
       implicit session: Session =>
         val q = for { 
           um <-  query filter(m => m.userId === userId && m.messageId === messageId)
         } yield (um.star, um.updatedUserId, um.updatedAt)
         
         q.update((false, userId, new DateTime()))
    }
  }
  
  def markImportant(messageId: Long, userId: Long) = {
    DB.withSession {
       implicit session: Session =>
         val q = for { 
           um <-  query filter(m => m.userId === userId && m.messageId === messageId)
         } yield (um.important, um.updatedUserId, um.updatedAt)
         
         q.update((true, userId, new DateTime()))
    }
  }
  
  def removeImportant(messageId: Long, userId: Long) = {
    DB.withSession {
       implicit session: Session =>
         val q = for { 
           um <-  query filter(m => m.userId === userId && m.messageId === messageId)
         } yield (um.important, um.updatedUserId, um.updatedAt)
         
         q.update((false, userId, new DateTime())) 
    }
  }
  
  def markRead(messageId: Long, userId: Long) = {
    DB.withSession {
       implicit session: Session =>
         val q = for { 
           um <-  query filter(m => m.userId === userId && m.messageId === messageId)
         } yield (um.read, um.updatedUserId, um.updatedAt)
         
         q.update((true, userId, new DateTime())) 
    }
  }
  
  def markReplied(messageId: Long, userId: Long) = {
    DB.withSession {
       implicit session: Session =>
         val q = for { 
           um <-  query filter(m => m.userId === userId && m.messageId === messageId)
         } yield (um.replied, um.updatedUserId, um.updatedAt)
         
         q.update((true, userId, new DateTime()))
    }
  }
  
  def moveMessageBox(messageId: Long, messageBoxId: Long, userId: Long) = {
    DB.withSession {
       implicit session: Session =>
         val q = for { 
           um <-  query filter(m => m.userId === userId && m.messageId === messageId)
         } yield (um.messageBoxId, um.updatedUserId, um.updatedAt)
         
         q.update((messageBoxId, userId, new DateTime()))
    }
  }
  
}

