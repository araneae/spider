package models.repositories

import models.tables._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.dtos._

object UserRepository {
  
  val query = TableQuery[Users]
  
  def findByEmail(email : String) : Option[User] = {
    DB.withSession {
      implicit session =>
        val userList = findAll()
        userList.foreach(u => {
            if (u.email == email) {
              return Some(u)
            }
          }
        )
    }
    None
  }
  
  def find(id: Long): Option[User] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.id === id) firstOption
    }
  }
  
  def findUserId(email : String) : Option[Long] = {
    val user = findByEmail(email)
    user match {
      case Some(u) => u.id
      case None => None 
    }
  }
  
  def create(user: User) :Long = {
    DB.withSession {
      implicit session =>
        query returning query.map(_.id) += user
    }
  }
  
  def findAll() (implicit session: Session): Seq[User] = {
    DB.withSession {
      implicit session =>
        query.list
    }
  }
}

