package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.EmploymentType._
import enums.JobStatusType._
import enums.PaymentTermType._
import enums.CurrencyType._
import enums.TaxTermType._
import enums.SalaryType._
import enums.BackgroundCheckType._

/**
 * Defines job requirements
 *  
 */
case class JobRequirementDTO(  
                 jobRequirementId: Option[Long],
                 companyId: Long,
                 code: String,
                 refNumber: Option[String],
                 title: String,
                 employmentType: EmploymentType,
                 industryId: Long,
                 location: String,
                 description: String,
                 status: JobStatusType,
                 positions: Int,
                 jobTitleId: Long,
                 xtn: JobRequirementXtnDTO) {
  def this(jobRequirement: JobRequirement, xtn: JobRequirementXtn) {
      this(jobRequirement.jobRequirementId,
           jobRequirement.companyId,
           jobRequirement.code,
           jobRequirement.refNumber,
           jobRequirement.title,
           jobRequirement.employmentType,
           jobRequirement.industryId,
           jobRequirement.location,
           jobRequirement.description,
           jobRequirement.status,
           jobRequirement.positions,
           jobRequirement.jobTitleId,
           JobRequirementXtnDTO(xtn))
  }
}

object JobRequirementDTO extends Function13[Option[Long], Long, String, Option[String], String, EmploymentType, Long, String, 
                            String, JobStatusType, Int, Long, JobRequirementXtnDTO, JobRequirementDTO]
{
    implicit val jobRequirementWrites : Writes[JobRequirementDTO] = (
            (JsPath \ "jobRequirementId").write[Option[Long]] and
            (JsPath \ "companyId").write[Long] and
            (JsPath \ "code").write[String] and
            (JsPath \ "refNumber").write[Option[String]] and
            (JsPath \ "title").write[String] and
            (JsPath \ "employmentType").write[EmploymentType] and
            (JsPath \ "industryId").write[Long] and
            (JsPath \ "location").write[String] and
            (JsPath \ "description").write[String] and
            (JsPath \ "status").write[JobStatusType] and
            (JsPath \ "positions").write[Int] and
            (JsPath \ "jobTitleId").write[Long] and
            (JsPath \ "xtn").write[JobRequirementXtnDTO]
    )(unlift(JobRequirementDTO.unapply))
      
    implicit val jobRequirementReads : Reads[JobRequirementDTO] = (
          (JsPath \ "jobRequirementId").readNullable[Long] and
          (JsPath \ "companyId").read[Long] and
          (JsPath \ "code").read[String] and
          (JsPath \ "refNumber").readNullable[String] and
          (JsPath \ "title").read[String] and
          (JsPath \ "employmentType").read[EmploymentType] and
          (JsPath \ "industryId").read[Long] and
          (JsPath \ "location").read[String] and
          (JsPath \ "description").read[String] and
          (JsPath \ "status").read[JobStatusType] and
          (JsPath \ "positions").read[Int] and
          (JsPath \ "jobTitleId").read[Long] and
          (JsPath \ "xtn").read[JobRequirementXtnDTO]
    )(JobRequirementDTO)
    
    def apply(jobRequirement: JobRequirement, jobRequirementXtn: JobRequirementXtn) = new JobRequirementDTO(jobRequirement, jobRequirementXtn)
}
