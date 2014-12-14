package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import enums.ContactStatus._

object FollowerRepository {
  
  val query = TableQuery[Followers]
  
  def create(follower: Follower) = {
    DB.withSession {
       implicit session: Session =>
         query.insert(follower)
    }
  }
  
  def find(subjectId: Long, followerId: Long): Option[Follower] = {
    DB.withSession {
      implicit session =>
        query.filter(s => s.subjectId === subjectId && s.followerId === followerId).firstOption
    }
  }
  
  def findAll(subjectId: Long): Seq[FollowerDTO] = {
    DB.withSession {
      implicit session =>
        val q = for {
            c <- query.filter(_.subjectId === subjectId)
            a <- c.follower
        } yield (c.subjectId, c.followerId, a.firstName, a.lastName, a.email)
         
        q.list.map{case (subjectId, followerId, firstName, lastName, email)
                => FollowerDTO(subjectId, followerId, firstName, lastName, email)}
    }
  }
  
  def findByToken(token: String): Option[Follower] = {
    DB.withSession {
      implicit session =>
        query.filter(s => s.token === token).firstOption
    }
  }
  
   def updateStatus(subjectId: Long, followerId: Long, status: ContactStatus, token: Option[String]) = {
    DB.withSession {
       implicit session: Session =>
         val q = for { u <- query if u.subjectId === subjectId && u.followerId === followerId } yield (u.status, u.token)
         q.update((status, token))
    }
  }
  
  def updateToken(subjectId: Long, followerId: Long, token: String) = {
    DB.withSession {
       implicit session: Session =>
         val q = for { u <- query if u.subjectId === subjectId && u.followerId === followerId } yield u.token
         q.update(Some(token))
    }
  }
  
  def delete(subjectId: Long, followerId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter( u => u.subjectId === subjectId && u.followerId === followerId).delete
    }
  }
}

