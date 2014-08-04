package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import enums.ContactStatus._

object ContactRepository {
  
  val query = TableQuery[Contacts]
  
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
        } yield (c.userId, c.contactUserId, a.firstName, a.lastName, a.email)
         
        q.list.map{case (userId, contactUserId, firstName, lastName, email) 
                => ContactFull(userId, contactUserId, firstName, lastName, email)}
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
         val q = for { u <- query if u.userId === userId && u.contactUserId === contactUserId } yield u.token
         q.update(token)
    }
  }
  
  def updateStatus(userId: Long, contactUserId: Long, status: ContactStatus, token: Option[String]) = {
    DB.withSession {
       implicit session: Session =>
         val q = for { u <- query if u.userId === userId && u.contactUserId === contactUserId } yield (u.status, u.token)
         q.update((status, token.getOrElse(null)))
    }
  }
  
  def delete(userId: Long, contactUserId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter( u => u.userId === userId && u.contactUserId === contactUserId).delete
    }
  }
}

