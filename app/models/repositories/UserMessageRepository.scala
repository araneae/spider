package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
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
         } yield (um.messageId, um.messageBoxId, m.subject, m.body, s.firstName, um.read, um.important, um.star)
        
         q.list.map{case (messageId, messageBoxId, subject, body, firstName, read, important, star) 
                => UserMessageFull(messageId, messageBoxId, subject, body, firstName, read, important, star)}
    }
  }
  
  def udate(userMessage: UserMessage) = {
    DB.withSession {
       implicit session: Session =>
         query filter(m => m.userId === userMessage.userId && m.messageId === userMessage.messageId) update userMessage 
    }
  }
  
  def markStar(messageId: Long) = {
    DB.withSession {
       implicit session: Session =>
         query filter(m => m.messageId === messageId) map(m => m.star) update(true) 
    }
  }
  
  def removeStar(messageId: Long) = {
    DB.withSession {
       implicit session: Session =>
         query filter(m => m.messageId === messageId) map(m => m.star) update(false) 
    }
  }
  
  def markImportant(messageId: Long) = {
    DB.withSession {
       implicit session: Session =>
         query filter(m => m.messageId === messageId) map(m => m.important) update(true) 
    }
  }
  
  def removeImportant(messageId: Long) = {
    DB.withSession {
       implicit session: Session =>
         query filter(m => m.messageId === messageId) map(m => m.important) update(false) 
    }
  }
  
}

