package models.tables

import play.api.db.slick.Config.driver.simple._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import models.tables._
import enums.ContactStatus._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

class Followers(tag: Tag) extends Table[Follower](tag, "follower") {

  def subjectId = column[Long]("subject_id")
  
  def followerId = column[Long]("follower_id")
  
  def status = column[ContactStatus]("status", O.Default(PENDING))
  
  def token = column[Option[String]]("token")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (subjectId, followerId, status, token, createdUserId, createdAt, updatedUserId, updatedAt) <> (Follower.tupled, Follower.unapply)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_follower_subject_id_follower_id", (subjectId, followerId))
  
  def subject = foreignKey("fk_on_follower_subject_id", subjectId, TableQuery[Users])(_.userId)
  
  def follower = foreignKey("fk_on_follower_follower_id", followerId, TableQuery[Users])(_.userId)
  
  def createdBy = foreignKey("fk_on_follower_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_on_follower_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}