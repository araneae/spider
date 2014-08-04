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
  
  def findAll(userId: Long): Seq[UserMessage] = {
    DB.withSession {
      implicit session =>
       query.filter(_.userId === userId).list
    }
  }
  
  def udate(userMessage: UserMessage) = {
    DB.withSession {
       implicit session: Session =>
         query filter(m => m.userId === userMessage.userId && m.messageId === userMessage.messageId) update userMessage 
    }
  }
  
}

