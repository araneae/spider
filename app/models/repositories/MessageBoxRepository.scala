package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import enums.MessageBoxType._
import org.joda.time.DateTime

object MessageBoxRepository {
  
  val queryMessageBoxes = TableQuery[MessageBoxes]
  val queryUserMessages = TableQuery[UserMessages]
  
  def create(messageBox: MessageBox): Long = {
    DB.withSession {
       implicit session: Session =>
         queryMessageBoxes returning queryMessageBoxes.map(_.messageBoxId) += messageBox
    }
  }
  
  def createDefaults(userId: Long, createdUserId: Long) = {
    MessageBoxRepository.create(MessageBox(None, userId, INBOX, "Inbox", createdUserId))
    MessageBoxRepository.create(MessageBox(None, userId, SENTITEMS, "Sent", createdUserId)) 
    MessageBoxRepository.create(MessageBox(None, userId, TRASH, "Trash", createdUserId)) 
  }
  
  def findByType(userId: Long, messageBoxType: MessageBoxType): Option[MessageBox] = {
    DB.withSession {
      implicit session =>
       queryMessageBoxes.filter(b => b.userId === userId && b.messageBoxType === messageBoxType) firstOption
    }
  }
  
  def findInbox(userId: Long): Option[MessageBox] = {
    findByType(userId, INBOX)
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
       queryMessageBoxes.filter(_.messageBoxId === messageBoxId) firstOption
    }
  }
  
  def findAll(userId: Long): Seq[MessageBoxDTO] = {
    DB.withSession {
      implicit session =>
        val q = for {
          (mb, um) <- queryMessageBoxes leftJoin queryUserMessages on (_.messageBoxId === _.messageBoxId) if mb.userId === userId
        } yield (mb, um)
        
        val q2 = q.groupBy(_._1)
        val q3 = q2.map{
             case(mb, group) 
                    => (mb.messageBoxId, mb.messageBoxType, mb.name, mb.createdAt, group.map(u => u._2.messageId).length)}
        
        q3.list.map {
                case(messageBoxId, messageBoxType, name, createdAt, messageCount) 
                          => MessageBoxDTO(messageBoxId, messageBoxType, name, createdAt, messageCount)
                }
    }
  }
  
  def rename(messageBoxId: Long, name: String, userId: Long) = {
    DB.withSession {
       implicit session: Session =>
         val q = for {
           mb <- queryMessageBoxes filter(m => m.messageBoxId === messageBoxId)
         } yield(mb.name, mb.updatedUserId, mb.updatedAt)
         
         q.update((name, Some(userId), Some(new DateTime())))
    }
  }
}

