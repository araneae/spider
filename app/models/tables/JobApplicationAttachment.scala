package models.tables

import play.api.db.slick.Config.driver.simple._
import models.dtos._
import utils.JodaToSqlMapper._
import org.joda.time.DateTime
import enums.AttachmentType._

/**
 * Job application attachment
 * 
 */

class JobApplicationAttachments(tag: Tag) extends Table[JobApplicationAttachment](tag, "job_application_attachment") {

  def jobApplicationAttachmentId = column[Long]("job_application_attachment_id", O.PrimaryKey, O.AutoInc)
  
  def jobApplicationId = column[Long]("job_application_id")
  
  def attachmentType = column[AttachmentType]("attachment_type")
  
  def documentId = column[Long]("document_id")
  
  def createdUserId = column[Long]("created_user_id")
  
  def createdAt = column[DateTime]("created_at")
  
  def updatedUserId = column[Option[Long]]("updated_user_id")
  
  def updatedAt = column[Option[DateTime]]("updated_at")
  
  override def * = (jobApplicationAttachmentId.?, jobApplicationId, attachmentType, documentId,
                            createdUserId, createdAt, updatedUserId, updatedAt) <> (JobApplicationAttachment.tupled, JobApplicationAttachment.unapply)
  
  // foreign keys and indexes
  def jobApplication = foreignKey("fk_job_application_attachment_on_application_id", jobApplicationId, TableQuery[JobApplications])(_.jobApplicationId)
  
  def document = foreignKey("fk_job_application_attachment_on_document_id", documentId, TableQuery[Documents])(_.documentId)
  
  def createdBy = foreignKey("fk_job_application_attachment_on_created_user_id", createdUserId, TableQuery[Users])(_.userId)
  
  def updatedBy = foreignKey("fk_job_application_attachment_on_updated_user_id", updatedUserId, TableQuery[Users])(_.userId)
}
