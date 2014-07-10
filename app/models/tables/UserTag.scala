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

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  
  def userId = column[Long]("user_id", O.NotNull)
  
  def name = column[String]("name", O.NotNull)
  
  override def * = (id.?, userId, name) <> (UserTag.tupled, UserTag.unapply)
  
  // foreign keys and indexes
  def pk = primaryKey("pk_on_user_tag_id_user_id", (id, userId))

}
