package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.EmploymentType._
import enums.JobStatusType._
import enums.SalaryTermType._
import enums.CurrencyType._

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
                 salaryMin: Double,
                 salaryMax: Double,
                 currency: CurrencyType,
                 salaryTerm: SalaryTermType,
                 description: String,
                 status: JobStatusType,
                 positions: Int,
                 jobTitleId: Long)

object JobRequirementDTO extends Function16[Option[Long], Long, String, Option[String], String, EmploymentType, Long, String, Double, Double, CurrencyType, 
                            SalaryTermType, String, JobStatusType, Int, Long, JobRequirementDTO]
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
            (JsPath \ "salaryMin").write[Double] and
            (JsPath \ "salaryMax").write[Double] and
            (JsPath \ "currency").write[CurrencyType] and
            (JsPath \ "salaryTerm").write[SalaryTermType] and
            (JsPath \ "description").write[String] and
            (JsPath \ "status").write[JobStatusType] and
            (JsPath \ "positions").write[Int] and
            (JsPath \ "jobTitleId").write[Long]
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
          (JsPath \ "salaryMin").read[Double] and
          (JsPath \ "salaryMax").read[Double] and
          (JsPath \ "currency").read[CurrencyType] and
          (JsPath \ "salaryTerm").read[SalaryTermType] and
          (JsPath \ "description").read[String] and
          (JsPath \ "status").read[JobStatusType] and
          (JsPath \ "positions").read[Int] and
          (JsPath \ "jobTitleId").read[Long]
    )(JobRequirementDTO)
    
}
