package models.repositories

import models.tables._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.dtos._
import org.joda.time.DateTime

object UserRepository {
  
  val query = TableQuery[Users]
  
  def findByEmail(email : String) : Option[User] = {
    DB.withSession {
      implicit session =>
         query.filter(u => u.email.toLowerCase === email.toLowerCase).firstOption
    }
  }
  
  def find(userId: Long): Option[User] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.userId === userId) firstOption
    }
  }
  
  def findByActivationToken(token: String): Option[User] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.activationToken === token) firstOption
    }
  }
  
  def findUserId(email : String) : Option[Long] = {
    val user = findByEmail(email)
    user match {
      case Some(u) => u.userId
      case None => None 
    }
  }
  
  def activate(userId: Long) = {
    DB.withSession {
       implicit session: Session =>
          val q = for {
           u <-  query filter(_.userId === userId) 
          } yield (u.verified, u.updatedAt)
          
          q.update((true, new DateTime()))
    }
  }
  
  def create(user: User) :Long = {
    DB.withSession {
      implicit session =>
        query returning query.map(_.userId) += user
    }
  }
  
  def findAll() (implicit session: Session): Seq[User] = {
    DB.withSession {
      implicit session =>
        query.list
    }
  }
  
  def updateOneTimePassword(userId: Long, otp: Option[String], date: Option[DateTime]) = {
    DB.withSession {
      implicit session =>
        val q = for {
          u <- query filter (_.userId === userId)
        } yield (u.otp.?, u.otpExpiredAt, u.updatedAt)
        
        q update ((otp, date, new DateTime()))
    }
  }
  
  def resetPassward(userId: Long, password: String) = {
    DB.withSession {
      implicit session =>
        val q = for {
          u <- query filter (_.userId === userId)
        } yield (u.password, u.otp.?, u.otpExpiredAt, u.updatedAt)
        
        q update ((password, None, None, new DateTime()))
    }
  }
}

