package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import utils._

object ConnectionRepository {
  
  val followers = TableQuery[Followers]
  val contacts = TableQuery[Contacts]
  
  def findAll(userId: Long): Seq[ConnectionDTO] = {
    DB.withSession {
       implicit session: Session =>
         val q1 = for {
            f <- followers.filter(a => a.followerId ===  userId)
            u <- f.subject
         } yield (u.userId, u.firstName, u.lastName)
        
         val q2 = for {
            c <- contacts.filter(a => a.userId ===  userId)
            u <- c.contact
         } yield (u.userId, u.firstName, u.lastName)
        
         val unionQuery = q1 union q2
         unionQuery.list.map{case (userId, firstName, lastName) 
                             => ConnectionDTO(userId, s"${firstName} ${lastName}")
                           }
    }
  }
  
}

