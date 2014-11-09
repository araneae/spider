package models.tables

import play.api.db.slick.Config.driver.simple._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime
import models.dtos._
import enums.GenderType._
import enums.MaritalStatusType._
import enums.EducationLevelType._

class UserPersonalProfiles(tag: Tag) extends Table[UserPersonalProfile](tag, "user_personal_profile") {

  def userPersonalProfileId = column[Option[Long]]("user_personal_profile_id", O.PrimaryKey, O.AutoInc)
  
  def aboutMe = column[Option[String]]("aboutMe", O.Nullable)
  
  def picture = column[Option[String]]("picture", O.Nullable)
  
  def mobile = column[Option[String]]("mobile", O.Nullable)
  
  def alternativeEmail = column[Option[String]]("alternative_email", O.Nullable)
  
  def gender = column[Option[GenderType]]("gender", O.Nullable)
  
  def maritalStatus = column[Option[MaritalStatusType]]("marital_status", O.Nullable)
  
  def birthYear = column[Option[Int]]("birth_year", O.Nullable)
  
  def birthMonth = column[Option[Int]]("birth_month", O.Nullable)
  
  def birthDay = column[Option[Int]]("birth_day", O.Nullable)
  
  def xrayTerms = column[String]("xray_terms", O.Nullable)

  def createdAt = column[DateTime]("created_at", O.NotNull)
  
  def updatedAt = column[Option[DateTime]]("updated_at", O.Nullable)
  
  override def * = (userPersonalProfileId, aboutMe, picture, mobile, alternativeEmail, gender, maritalStatus, birthYear, birthDay, birthMonth, xrayTerms, createdAt, updatedAt) <> (UserPersonalProfile.tupled, UserPersonalProfile.unapply)
  
  // foreign keys and indexes

}
 