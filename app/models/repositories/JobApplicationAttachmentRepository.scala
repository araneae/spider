package models.repositories

import models.tables._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.dtos._
import org.joda.time.DateTime
import enums.JobStatusType._
import enums._

object JobApplicationAttachmentRepository {
  
  val query = TableQuery[JobApplicationAttachments]
  
  def create(jobApplicationAttachment: JobApplicationAttachment) = {
    DB.withSession {
       implicit session: Session =>
         query returning query.map(_.jobApplicationAttachmentId) += jobApplicationAttachment
    }
  }
  
  def update(jobApplicationAttachment: JobApplicationAttachment) = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.jobApplicationAttachmentId === jobApplicationAttachment.jobApplicationAttachmentId) update jobApplicationAttachment
    }
  }
  
  def find(jobApplicationAttachmentId: Long): Option[JobApplicationAttachment] = {
    DB.withSession {
       implicit session: Session =>
         query filter (_.jobApplicationAttachmentId === jobApplicationAttachmentId) firstOption
    }
  }
  
  def findByDocumentId(documentId: Long): Seq[JobApplicationAttachment] = {
    DB.withSession {
       implicit session: Session =>
         query filter (_.documentId === documentId) list
    }
  }
  
  def delete(jobApplicationAttachmentId: Long) = {
    DB.withSession {
       implicit session: Session =>
           query filter(_.jobApplicationAttachmentId === jobApplicationAttachmentId) delete
    }
  }
}

