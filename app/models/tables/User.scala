package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime

class Users(tag: Tag) extends Table[User](tag, "user") {

  def userId = column[Long]("user_id", O.PrimaryKey, O.AutoInc)
  
  def firstName = column[String]("first_name")
  
  def lastName = column[String]("last_name")
  
  def email = column[String]("email")
  
  def password = column[String]("password")
  
  def countryId = column[Long]("country_id")
  
  def activationToken = column[String]("activationToken")
  
  def verified = column[Boolean]("verified")
  
  def lastLogon = column[DateTime]("last_logon")
  
  def userProfilePersonalId = column[Option[Long]]("user_profile_personal_id")
  
  def otp = column[Option[String]]("otp")
  
  def otpExpiredAt = column[Option[DateTime]]("otp_expired_at")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (userId.?, firstName, lastName, email, password, countryId, activationToken, verified, lastLogon, userProfilePersonalId, otp, otpExpiredAt, createdAt, updatedAt) <> (User.tupled, User.unapply)
  
  // foreign keys and indexes
  def uniqueEmail = index("idx_user_on_email_unique", email, unique = true)
  
  def country = foreignKey("fk_on_user_country_id", countryId, TableQuery[Countries])(_.countryId)
  
  def userProfilePersonal = foreignKey("fk_on_user_user_profile_personal_id", userProfilePersonalId, TableQuery[UserProfilePersonals])(_.userProfilePersonalId)
  
   //def bs = AToB.filter(_.aId === id).flatMap(_.bFK)
  // https://groups.google.com/forum/#!topic/scalaquery/l-SMiyNOIJA
}
 