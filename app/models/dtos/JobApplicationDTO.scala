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
case class JobApplicationDTO(  
                 jobApplicationId: Option[Long],
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
                 postDate: Option[DateTime]) {
  def this(jobApplication: JobApplication) {
      this(jobApplication.jobApplicationId,
           jobApplication.companyId,
           jobApplication.code,
           jobApplication.refNumber,
           jobApplication.title,
           jobApplication.employmentType,
           jobApplication.industryId,
           jobApplication.location,
           jobApplication.description,
           jobApplication.status,
           jobApplication.positions,
           jobApplication.jobTitleId,
           jobApplication.postDate)
  }
}

object JobApplicationDTO extends Function13[Option[Long], Long, String, Option[String], String, EmploymentType, Long, String, 
                            String, JobStatusType, Int, Long, Option[DateTime], JobApplicationDTO]
{
    implicit val jobApplicationWrites : Writes[JobApplicationDTO] = (
            (JsPath \ "jobApplicationId").write[Option[Long]] and
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
            (JsPath \ "postDate").write[Option[DateTime]]
    )(unlift(JobApplicationDTO.unapply))
      
    implicit val jobApplicationReads : Reads[JobApplicationDTO] = (
          (JsPath \ "jobApplicationId").readNullable[Long] and
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
          (JsPath \ "postDate").readNullable[DateTime]
    )(JobApplicationDTO)
    
    def apply(jobApplication: JobApplication) = new JobApplicationDTO(jobApplication)
}
