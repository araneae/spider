package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

class Users(tag: Tag) extends Table[User](tag, "user") {

  def userId = column[Long]("user_id", O.PrimaryKey, O.AutoInc)
  
  def firstName = column[String]("first_name", O.NotNull)
  
  def lastName = column[String]("last_name", O.NotNull)
  
  def email = column[String]("email", O.NotNull)
  
  def password = column[String]("password", O.NotNull)
  
  def countryId = column[Long]("country_id", O.NotNull)
  
  def activationToken = column[String]("activationToken", O.NotNull)
  
  def verified = column[Boolean]("verified", O.NotNull)
  
  def otp = column[String]("otp", O.Nullable)
  
  def otpExpiredAt = column[Option[DateTime]]("otp_expired_at", O.Nullable)
  
  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedAt = column[DateTime]("updated_at", O.Nullable)
  
  override def * = (userId.?, firstName, lastName, email, password, countryId, activationToken, verified, otp.?, otpExpiredAt, createdAt, updatedAt.?) <> (User.tupled, User.unapply)
  
  // foreign keys and indexes
  def uniqueEmail = index("idx_user_on_email_unique", email, unique = true)
  
  def country = foreignKey("fk_on_user_country_id", countryId, TableQuery[Countries])(_.countryId)
  
   //def bs = AToB.filter(_.aId === id).flatMap(_.bFK)
  // https://groups.google.com/forum/#!topic/scalaquery/l-SMiyNOIJA
}
 