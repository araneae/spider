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
 * Defines public view of a job requirement
 *  
 */
case class JobDTO(  
                 jobRequirementId: Long,
                 companyName: String,
                 employmentType: EmploymentType,
                 industryName: String,
                 location: String,
                 description: String,
                 positions: Int,
                 jobTitle: String,
                 postDate: Option[DateTime],
                 targetStartDate: Option[DateTime],
                 targetEndDate: Option[DateTime],
                 salaryType: SalaryType,
                 salaryMin: Option[Double],
                 salaryMax: Option[Double],
                 currency: CurrencyType,
                 taxTerm: TaxTermType,
                 status: JobStatusType,
                 paymentTerm: PaymentTermType,
                 backgroundCheck: BackgroundCheckType) {
  def this(company: Company, industry: Industry, jobTitle: JobTitle, jobRequirement: JobRequirement, xtn: JobRequirementXtn) {
      this(jobRequirement.jobRequirementId.get,
           company.name,
           jobRequirement.employmentType,
           industry.name,
           jobRequirement.location,
           jobRequirement.description,
           jobRequirement.positions,
           jobTitle.name,
           jobRequirement.postDate,
           xtn.targetStartDate,
           xtn.targetEndDate,
           xtn.salaryType,
           xtn.salaryMin,
           xtn.salaryMax,
           xtn.currency,
           xtn.taxTerm,
           jobRequirement.status,
           xtn.paymentTerm,
           xtn.backgroundCheck)
  }
}

object JobDTO extends Function19[Long, String, EmploymentType, String, String, String, Int, String, Option[DateTime], Option[DateTime], 
                                        Option[DateTime], SalaryType, Option[Double], Option[Double], CurrencyType, TaxTermType, JobStatusType,
                                              PaymentTermType, BackgroundCheckType, JobDTO]
{
    implicit val jobWrites : Writes[JobDTO] = (
            (JsPath \ "jobRequirementId").write[Long] and
            (JsPath \ "companyName").write[String] and
            (JsPath \ "employmentType").write[EmploymentType] and
            (JsPath \ "industryName").write[String] and
            (JsPath \ "location").write[String] and
            (JsPath \ "description").write[String] and
            (JsPath \ "positions").write[Int] and
            (JsPath \ "jobTitle").write[String] and
            (JsPath \ "postDate").write[Option[DateTime]] and
            (JsPath \ "targetStartDate").write[Option[DateTime]] and
            (JsPath \ "targetEndDate").write[Option[DateTime]] and
            (JsPath \ "salaryType").write[SalaryType] and
            (JsPath \ "salaryMin").write[Option[Double]] and
            (JsPath \ "salaryMax").write[Option[Double]] and
            (JsPath \ "currency").write[CurrencyType] and
            (JsPath \ "taxTerm").write[TaxTermType] and
            (JsPath \ "status").write[JobStatusType] and
            (JsPath \ "paymentTerm").write[PaymentTermType] and
            (JsPath \ "backgroundCheck").write[BackgroundCheckType]
    )(unlift(JobDTO.unapply))
      
    implicit val jobReads : Reads[JobDTO] = (
          (JsPath \ "jobRequirementId").read[Long] and
          (JsPath \ "companyName").read[String] and
          (JsPath \ "employmentType").read[EmploymentType] and
          (JsPath \ "industryName").read[String] and
          (JsPath \ "location").read[String] and
          (JsPath \ "description").read[String] and
          (JsPath \ "positions").read[Int] and
          (JsPath \ "jobTitle").read[String] and
          (JsPath \ "postDate").readNullable[DateTime] and
          (JsPath \ "targetStartDate").readNullable[DateTime] and
          (JsPath \ "targetEndDate").readNullable[DateTime] and
          (JsPath \ "salaryType").read[SalaryType] and
          (JsPath \ "salaryMin").readNullable[Double] and
          (JsPath \ "salaryMax").readNullable[Double] and
          (JsPath \ "currency").read[CurrencyType] and
          (JsPath \ "taxTerm").read[TaxTermType] and
          (JsPath \ "status").read[JobStatusType] and
          (JsPath \ "paymentTerm").read[PaymentTermType] and
          (JsPath \ "backgroundCheck").read[BackgroundCheckType]
    )(JobDTO)
    
    def apply(company: Company, industry: Industry, jobTitle: JobTitle, jobRequirement: JobRequirement, xtn: JobRequirementXtn) 
            = new JobDTO(company, industry, jobTitle, jobRequirement, xtn)
}
