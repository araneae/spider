package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.AttachmentType._

/**
 * Defines attachment DTO
 *  
 */
case class AttachmentDTO(
                 attachmentType: AttachmentType,
                 documentId: Long) {
   def this(jobApplicationAttachment: JobApplicationAttachment) {
       this(jobApplicationAttachment.attachmentType,
           jobApplicationAttachment.documentId)
   }
}

object AttachmentDTO extends Function2[AttachmentType, Long, AttachmentDTO]
{
    implicit val attachmentDTOWrites : Writes[AttachmentDTO] = (
            (JsPath \ "attachmentType").write[AttachmentType] and
            (JsPath \ "documentId").write[Long]
    )(unlift(AttachmentDTO.unapply))
      
    implicit val attachmentDTOReads : Reads[AttachmentDTO] = (
          (JsPath \ "attachmentType").read[AttachmentType] and
          (JsPath \ "documentId").read[Long]
    )(AttachmentDTO)
    
    def apply(jobApplicationAttachment: JobApplicationAttachment) = new AttachmentDTO(jobApplicationAttachment)
}
