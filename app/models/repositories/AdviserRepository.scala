package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import enums.ContactStatus._

object AdviserRepository {
  
  val query = TableQuery[Advisers]
  
  def create(adviser: Adviser) = {
    DB.withSession {
       implicit session: Session =>
         query.insert(adviser)
    }
  }
  
  def find(userId: Long, adviserUserId: Long): Option[Adviser] = {
    DB.withSession {
      implicit session =>
        query.filter(s => s.userId === userId && s.adviserUserId === adviserUserId).firstOption
    }
  }
  
  def findAll(userId: Long): Seq[AdviserFull] = {
    DB.withSession {
      implicit session =>
        val q = for {
            c <- query.filter(_.userId === userId)
            a <- c.adviser
        } yield (c.userId, c.adviserUserId, a.firstName, a.lastName, a.email)
         
        q.list.map{case (userId, adviserUserId, firstName, lastName, email)
                => AdviserFull(userId, adviserUserId, firstName, lastName, email)}
    }
  }
  
  def findByToken(token: String): Option[Adviser] = {
    DB.withSession {
      implicit session =>
        query.filter(s => s.token === token).firstOption
    }
  }
  
   def updateStatus(userId: Long, adviserUserId: Long, status: ContactStatus, token: Option[String]) = {
    DB.withSession {
       implicit session: Session =>
         val q = for { u <- query if u.userId === userId && u.adviserUserId === adviserUserId } yield (u.status, u.token)
         q.update((status, token.getOrElse(null)))
    }
  }
  
  def updateToken(userId: Long, adviserUserId: Long, token: String) = {
    DB.withSession {
       implicit session: Session =>
         val q = for { u <- query if u.userId === userId && u.adviserUserId === adviserUserId } yield u.token
         q.update(token)
    }
  }
  
  def delete(userId: Long, adviserUserId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter( u => u.userId === userId && u.adviserUserId === adviserUserId).delete
    }
  }
}

