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
  
  def xrayTerms = column[String]("xray_terms")
  
  def aboutMe = column[Option[String]]("aboutMe")
  
  def pictureFile = column[Option[String]]("picture_file")
  
  def physicalFile = column[Option[String]]("physical_file")
  
  def mobile = column[Option[String]]("mobile")
  
  def alternateEmail = column[Option[String]]("alternate_email")
  
  def gender = column[Option[GenderType]]("gender")
  
  def maritalStatus = column[Option[MaritalStatusType]]("marital_status")
  
  def birthYear = column[Option[Int]]("birth_year")
  
  def birthMonth = column[Option[Int]]("birth_month")
  
  def birthDay = column[Option[Int]]("birth_day")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (userProfilePersonalId.?, xrayTerms, aboutMe, pictureFile, physicalFile, mobile, alternateEmail, gender, maritalStatus, birthYear, birthDay, birthMonth, createdAt, updatedAt) <> (UserProfilePersonal.tupled, UserProfilePersonal.unapply)
  
  // foreign keys and indexes

}
 