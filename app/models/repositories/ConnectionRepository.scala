package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._

object ConnectionRepository {
  
  val advisers = TableQuery[Advisers]
  val contacts = TableQuery[Contacts]
  
  def findAll(userId: Long): Seq[Connection] = {
    DB.withSession {
       implicit session: Session =>
         val q1 = for {
            u <- advisers.filter(a => a.userId ===  userId)
            a <- u.adviser
         } yield (a.id, a.email)
        
         val q2 = for {
            u <- contacts.filter(a => a.userId ===  userId)
            c <- u.contact
         } yield (c.id, c.email)
        
         val unionQuery = q1 union q2
         unionQuery.list.map{case (userId, email) => Connection(userId, email)}
    }
  }
  
}

