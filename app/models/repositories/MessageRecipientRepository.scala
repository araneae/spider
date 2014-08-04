package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._

object MessageRecipientRepository {
  
  val query = TableQuery[MessageRecipients]
  
  def create(recipient: MessageRecipient) = {
    DB.withSession {
       implicit session: Session =>
         query.insert(recipient)
    }
  }
  
  def find(messageId: Long): Seq[MessageRecipient] = {
    DB.withSession {
      implicit session =>
       query.filter(_.messageId === messageId) list
    }
  }
  
}

