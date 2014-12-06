package models.repositories

import models.tables._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.dtos._
import org.joda.time.DateTime

object UserProfilePersonalRepository {
  
  val query = TableQuery[UserProfilePersonals]
  
  def find(userProfilePersonalId: Long): Option[UserProfilePersonal] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.userProfilePersonalId === userProfilePersonalId) firstOption
    }
  }
  
  def create(userProfilePersonal: UserProfilePersonal): Long = {
    DB.withSession {
      implicit session =>
        query returning query.map(_.userProfilePersonalId) += userProfilePersonal
    }
  }
  
  def update(userProfilePersonal: UserProfilePersonal) = {
    DB.withSession {
      implicit session =>
        query filter(_.userProfilePersonalId === userProfilePersonal.userProfilePersonalId) update userProfilePersonal
    }
  }
  
}

