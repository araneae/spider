package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.RelocationType._
import enums.TravelingType._

/**
 * Defines job application DTO
 *  
 */
case class JobApplicationDTO(  
                 jobApplicationId: Option[Long],
                 companyId: Long,
                 jobRequirementId: Long,
                 phone: String,
                 availableInWeeks: Int,
                 relocation: RelocationType,
                 traveling: TravelingType,
                 message: String,
                 attachments: Seq[AttachmentDTO]) {
  def this(jobApplication: JobApplication, jobApplicationAttachments: Seq[JobApplicationAttachment]) {
      this(jobApplication.jobApplicationId,
           jobApplication.companyId,
           jobApplication.jobRequirementId,
           jobApplication.phone,
           jobApplication.availableInWeeks,
           jobApplication.relocation,
           jobApplication.traveling,
           jobApplication.message,
           jobApplicationAttachments.map { x => AttachmentDTO(x) })
  }
}

object JobApplicationDTO extends Function9[Option[Long], Long, Long, String, Int, RelocationType, 
                                         TravelingType, String, Seq[AttachmentDTO], JobApplicationDTO]
{
    implicit val jobApplicationDTOWrites : Writes[JobApplicationDTO] = (
            (JsPath \ "jobApplicationId").write[Option[Long]] and
            (JsPath \ "companyId").write[Long] and
            (JsPath \ "jobRequirementId").write[Long] and
            (JsPath \ "phone").write[String] and
            (JsPath \ "availableInWeeks").write[Int] and
            (JsPath \ "relocation").write[RelocationType] and
            (JsPath \ "traveling").write[TravelingType] and
            (JsPath \ "message").write[String] and
            (JsPath \ "attachments").write[Seq[AttachmentDTO]]
    )(unlift(JobApplicationDTO.unapply))
      
    implicit val jobApplicationDTOReads : Reads[JobApplicationDTO] = (
          (JsPath \ "jobApplicationId").readNullable[Long] and
          (JsPath \ "companyId").read[Long] and
          (JsPath \ "jobRequirementId").read[Long] and
          (JsPath \ "phone").read[String] and
          (JsPath \ "availableInWeeks").read[Int] and
          (JsPath \ "relocation").read[RelocationType] and
          (JsPath \ "traveling").read[TravelingType] and
          (JsPath \ "message").read[String] and
          (JsPath \ "attachments").read[Seq[AttachmentDTO]]
    )(JobApplicationDTO)
    
    def apply(jobApplication: JobApplication, jobApplicationAttachments: Seq[JobApplicationAttachment]) = new JobApplicationDTO(jobApplication, jobApplicationAttachments)
}
