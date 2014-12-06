package models.tables

import play.api.db.slick.Config.driver.simple._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime
import models.dtos._
import enums.GenderType._
import enums.MaritalStatusType._
import enums.EducationLevelType._

class UserProfilePersonals(tag: Tag) extends Table[UserProfilePersonal](tag, "user_profile_personal") {

  def userProfilePersonalId = column[Long]("user_profile_personal_id", O.PrimaryKey, O.AutoInc)
  
  def xrayTerms = column[String]("xray_terms", O.Nullable)
  
  def aboutMe = column[Option[String]]("aboutMe", O.Nullable)
  
  def picture = column[Option[String]]("picture", O.Nullable)
  
  def mobile = column[Option[String]]("mobile", O.Nullable)
  
  def alternateEmail = column[Option[String]]("alternate_email", O.Nullable)
  
  def gender = column[Option[GenderType]]("gender", O.Nullable)
  
  def maritalStatus = column[Option[MaritalStatusType]]("marital_status", O.Nullable)
  
  def birthYear = column[Option[Int]]("birth_year", O.Nullable)
  
  def birthMonth = column[Option[Int]]("birth_month", O.Nullable)
  
  def birthDay = column[Option[Int]]("birth_day", O.Nullable)
  

  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedAt = column[Option[DateTime]]("updated_at", O.Nullable)
  
  override def * = (userProfilePersonalId.?, xrayTerms, aboutMe, picture, mobile, alternateEmail, gender, maritalStatus, birthYear, birthDay, birthMonth, createdAt, updatedAt) <> (UserProfilePersonal.tupled, UserProfilePersonal.unapply)
  
  // foreign keys and indexes

}
 