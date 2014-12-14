package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._

object ConnectionRepository {
  
  val followers = TableQuery[Followers]
  val contacts = TableQuery[Contacts]
  
  def findAll(userId: Long): Seq[Connection] = {
    DB.withSession {
       implicit session: Session =>
         val q1 = for {
            u <- followers.filter(a => a.followerId ===  userId)
            a <- u.subject
         } yield (a.userId, a.email)
        
         val q2 = for {
            u <- contacts.filter(a => a.userId ===  userId)
            c <- u.contact
         } yield (c.userId, c.email)
        
         val unionQuery = q1 union q2
         unionQuery.list.map{case (userId, email) => Connection(userId, email)}
    }
  }
  
}

