package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.AttachmentType._

/**
 * Defines job application attachment
 *  
 */
case class JobApplicationAttachment(
                 jobApplicationAttachmentId: Option[Long],
                 jobApplicationId: Long,
                 attachmentType: AttachmentType,
                 documentId: Long,
                 createdUserId: Long,
                 createdAt: DateTime = new DateTime(),
                 updatedUserId: Option[Long] = None,
                 updatedAt: Option[DateTime] = None) {
   def this(jobApplicationAttachmentDTO: JobApplicationAttachmentDTO,
            createdUserId: Long, 
            createdAt: DateTime, 
            updatedUserId: Option[Long], 
            updatedAt: Option[DateTime]) {
       this(jobApplicationAttachmentDTO.jobApplicationAttachmentId,
           jobApplicationAttachmentDTO.jobApplicationId,
           jobApplicationAttachmentDTO.attachmentType,
           jobApplicationAttachmentDTO.documentId,
           createdUserId, 
           createdAt, 
           updatedUserId, 
           updatedAt)
   }
   
   def this(attachmentDTO: AttachmentDTO,
            jobApplicationId: Long,
            createdUserId: Long, 
            createdAt: DateTime, 
            updatedUserId: Option[Long], 
            updatedAt: Option[DateTime]) {
       this(None,
           jobApplicationId,
           attachmentDTO.attachmentType,
           attachmentDTO.documentId,
           createdUserId, 
           createdAt, 
           updatedUserId, 
           updatedAt)
   }
}

object JobApplicationAttachment extends Function8[Option[Long], Long, AttachmentType, Long, Long, DateTime, Option[Long], Option[DateTime], JobApplicationAttachment]
{
    implicit val jobApplicationWrites : Writes[JobApplicationAttachment] = (
            (JsPath \ "jobApplicationAttachmentId").write[Option[Long]] and
            (JsPath \ "jobApplicationId").write[Long] and
            (JsPath \ "attachmentType").write[AttachmentType] and
            (JsPath \ "documentId").write[Long] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(JobApplicationAttachment.unapply))
      
    implicit val jobApplicationReads : Reads[JobApplicationAttachment] = (
          (JsPath \ "jobApplicationAttachmentId").readNullable[Long] and
          (JsPath \ "jobApplicationId").read[Long] and
          (JsPath \ "attachmentType").read[AttachmentType] and
          (JsPath \ "documentId").read[Long] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(JobApplicationAttachment)
    
    def apply(jobApplicationAttachmentDTO: JobApplicationAttachmentDTO,
              createdUserId: Long,
              createdAt: DateTime,
              updatedUserId: Option[Long],
              updatedAt: Option[DateTime]) = new JobApplicationAttachment(jobApplicationAttachmentDTO, createdUserId, createdAt, updatedUserId, updatedAt)
    
    def apply(attachmentDTO: AttachmentDTO,
              jobApplicationId: Long,
              createdUserId: Long,
              createdAt: DateTime,
              updatedUserId: Option[Long],
              updatedAt: Option[DateTime]) = new JobApplicationAttachment(attachmentDTO, jobApplicationId, createdUserId, createdAt, updatedUserId, updatedAt)
}
