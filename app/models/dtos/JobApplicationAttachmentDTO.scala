package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.AttachmentType._

/**
 * Defines job application attachment DTO
 *  
 */
case class JobApplicationAttachmentDTO(
                 jobApplicationAttachmentId: Option[Long],
                 jobApplicationId: Long,
                 attachmentType: AttachmentType,
                 documentId: Long) {
   def this(jobApplicationAttachment: JobApplicationAttachment) {
       this(jobApplicationAttachment.jobApplicationAttachmentId,
           jobApplicationAttachment.jobApplicationId,
           jobApplicationAttachment.attachmentType,
           jobApplicationAttachment.documentId)
   }
}

object JobApplicationAttachmentDTO extends Function4[Option[Long], Long, AttachmentType, Long, JobApplicationAttachmentDTO]
{
    implicit val jobApplicationAttachmentDTOWrites : Writes[JobApplicationAttachmentDTO] = (
            (JsPath \ "jobApplicationAttachmentId").write[Option[Long]] and
            (JsPath \ "jobApplicationId").write[Long] and
            (JsPath \ "attachmentType").write[AttachmentType] and
            (JsPath \ "documentId").write[Long]
    )(unlift(JobApplicationAttachmentDTO.unapply))
      
    implicit val jobApplicationAttachmentDTOReads : Reads[JobApplicationAttachmentDTO] = (
          (JsPath \ "jobApplicationAttachmentId").readNullable[Long] and
          (JsPath \ "jobApplicationId").read[Long] and
          (JsPath \ "attachmentType").read[AttachmentType] and
          (JsPath \ "documentId").read[Long]
    )(JobApplicationAttachmentDTO)
    
    def apply(jobApplicationAttachment: JobApplicationAttachment) = new JobApplicationAttachmentDTO(jobApplicationAttachment)
}
