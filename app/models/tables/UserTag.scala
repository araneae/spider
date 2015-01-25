package models.tables

import play.api.db.slick.Config.driver.simple._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

/**
 * User can attach multiple tags - to organize 
 *    1) documents
 *    2) contacts
 * 
 */

class UserTags(tag: Tag) extends Table[UserTag](tag, "user_tag") {

  def userTagId = column[Long]("user_tag_Id", O.PrimaryKey, O.AutoInc)
  
  def userId = column[Long]("user_id")
  
  def name = column[String]("name")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (userTagId.?, userId, name, createdUserId, createdAt, updatedUserId, updatedAt) <> (UserTag.tupled, UserTag.unapply)
  
  // foreign keys and indexes
  def user = foreignKey("fk_user_tag_on_user_id", userId, TableQuery[Users])(_.userId)

  def createdBy = foreignKey("fk_user_tag_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_user_tag_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
