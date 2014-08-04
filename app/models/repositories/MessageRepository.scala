package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._

object MessageRepository {
  
  val query = TableQuery[Messages]
  
  def create(message: Message): Long = {
    DB.withSession {
       implicit session: Session =>
         query returning query.map(_.messageId) += message
    }
  }
  
  def find(messageId: Long): Option[Message] = {
    DB.withSession {
      implicit session =>
       query.filter(_.messageId === messageId) firstOption
    }
  }
  
  def udate(message: Message) = {
    DB.withSession {
       implicit session: Session =>
         query filter(m => m.messageId === message.messageId) update message 
    }
  }
  
}

