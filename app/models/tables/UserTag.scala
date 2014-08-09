package models.tables

import play.api.db.slick.Config.driver.simple._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import models.dtos._

/**
 * User can attach multiple tags - to organize 
 *    1) documents
 *    2) contacts
 * 
 */

class UserTags(tag: Tag) extends Table[UserTag](tag, "user_tag") {

  def userTagId = column[Long]("user_tag_Id", O.PrimaryKey, O.AutoInc)
  
  def userId = column[Long]("user_id", O.NotNull)
  
  def name = column[String]("name", O.NotNull)
  
  override def * = (userTagId.?, userId, name) <> (UserTag.tupled, UserTag.unapply)
  
  // foreign keys and indexes
  def user = foreignKey("fk_on_user_tag_user_id", userId, TableQuery[Users])(_.userId)

}
