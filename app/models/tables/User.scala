package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._

class Users(tag: Tag) extends Table[User](tag, "user") {

  def userId = column[Long]("user_id", O.PrimaryKey, O.AutoInc)
  
  def firstName = column[String]("first_name", O.NotNull)
  
  def lastName = column[String]("last_name", O.NotNull)
  
  def email = column[String]("email", O.NotNull)
  
  def password = column[String]("password", O.NotNull)
  
  override def * = (userId.?, firstName, lastName, email, password) <> (User.tupled, User.unapply)
  
  // foreign keys and indexes
  def uniqueEmail = index("idx_user_on_email_unique", email, unique = true)
  
   //def bs = AToB.filter(_.aId === id).flatMap(_.bFK)
  // https://groups.google.com/forum/#!topic/scalaquery/l-SMiyNOIJA
}
 