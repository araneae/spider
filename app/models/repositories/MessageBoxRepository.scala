package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import enums.MessageBoxType._

object MessageBoxRepository {
  
  val query = TableQuery[MessageBoxes]
  
  def create(messageBox: MessageBox): Long = {
    DB.withSession {
       implicit session: Session =>
         query returning query.map(_.messageBoxId) += messageBox
    }
  }
  
  def createDefaults(userId: Long, createdUserId: Long) = {
    MessageBoxRepository.create(MessageBox(None, userId, INBOX, "Messages", createdUserId))
    MessageBoxRepository.create(MessageBox(None, userId, DRAFT, "Draft", createdUserId))
    MessageBoxRepository.create(MessageBox(None, userId, OUTBOX, "Outbox", createdUserId))
    MessageBoxRepository.create(MessageBox(None, userId, SENTITEMS, "Sent", createdUserId)) 
    MessageBoxRepository.create(MessageBox(None, userId, TRASH, "Trash", createdUserId)) 
  }
  
  def findByType(userId: Long, messageBoxType: MessageBoxType): Option[MessageBox] = {
    DB.withSession {
      implicit session =>
       query.filter(b => b.userId === userId && b.messageBoxType === messageBoxType) firstOption
    }
  }
  
  def findInbox(userId: Long): Option[MessageBox] = {
    findByType(userId, INBOX)
  }
  
  def findDraft(userId: Long): Option[MessageBox] = {
    findByType(userId, DRAFT)
  }
  
  def findOutbox(userId: Long): Option[MessageBox] = {
    findByType(userId, OUTBOX)
  }
  
  def findSentItems(userId: Long): Option[MessageBox] = {
    findByType(userId, SENTITEMS)
  }
  
  def findTrash(userId: Long): Option[MessageBox] = {
    findByType(userId, TRASH)
  }
  
  def find(messageBoxId: Long): Option[MessageBox] = {
    DB.withSession {
      implicit session =>
       query.filter(_.messageBoxId === messageBoxId) firstOption
    }
  }
  
  def findAll(userId: Long): Seq[MessageBox] = {
    DB.withSession {
      implicit session =>
       query.filter(_.userId === userId) list
    }
  }
  
  def udate(messageBox: MessageBox) = {
    DB.withSession {
       implicit session: Session =>
         query filter(m => m.messageBoxId === messageBox.messageBoxId.get) update messageBox 
    }
  }
  
}

