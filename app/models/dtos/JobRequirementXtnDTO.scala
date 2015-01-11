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
 * Defines job requirements extension
 *  
 */
case class JobRequirementXtnDTO(  
                 jobRequirementXtnId: Option[Long],
                 targetStartDate: Option[DateTime],
                 targetEndDate: Option[DateTime],
                 locationLat: Option[Double],
                 locationLng: Option[Double],
                 salaryType: SalaryType,
                 salaryMin: Option[Double],
                 salaryMax: Option[Double],
                 currency: CurrencyType,
                 taxTerm : TaxTermType,
                 paymentTerm: PaymentTermType,
                 backgroundCheck: BackgroundCheckType
                 ) {
  def this(jobRequirementXtn: JobRequirementXtn) {
      this(jobRequirementXtn.jobRequirementXtnId,
           jobRequirementXtn.targetStartDate,
           jobRequirementXtn.targetEndDate,
           jobRequirementXtn.locationLat,
           jobRequirementXtn.locationLng,
           jobRequirementXtn.salaryType,
           jobRequirementXtn.salaryMin,
           jobRequirementXtn.salaryMax,
           jobRequirementXtn.currency,
           jobRequirementXtn.taxTerm,
           jobRequirementXtn.paymentTerm,
           jobRequirementXtn.backgroundCheck)
  }
}

object JobRequirementXtnDTO extends Function12[Option[Long], Option[DateTime], Option[DateTime], Option[Double], Option[Double], SalaryType, Option[Double],
            Option[Double], CurrencyType, TaxTermType, PaymentTermType, BackgroundCheckType, JobRequirementXtnDTO]
{
    implicit val jobRequirementWrites : Writes[JobRequirementXtnDTO] = (
            (JsPath \ "jobRequirementXtnId").write[Option[Long]] and
            (JsPath \ "targetStartDate").write[Option[DateTime]] and
            (JsPath \ "targetEndDate").write[Option[DateTime]] and
            (JsPath \ "locationLat").write[Option[Double]] and
            (JsPath \ "locationLng").write[Option[Double]] and
            (JsPath \ "salaryType").write[SalaryType] and
            (JsPath \ "salaryMin").write[Option[Double]] and
            (JsPath \ "salaryMax").write[Option[Double]] and
            (JsPath \ "currency").write[CurrencyType] and
            (JsPath \ "taxTerm").write[TaxTermType] and
            (JsPath \ "paymentTerm").write[PaymentTermType] and
            (JsPath \ "backgroundCheck").write[BackgroundCheckType]
    )(unlift(JobRequirementXtnDTO.unapply))
      
    implicit val jobRequirementReads : Reads[JobRequirementXtnDTO] = (
            (JsPath \ "jobRequirementXtnId").readNullable[Long] and
            (JsPath \ "targetStartDate").readNullable[DateTime] and
            (JsPath \ "targetEndDate").readNullable[DateTime] and
            (JsPath \ "locationLat").readNullable[Double] and
            (JsPath \ "locationLng").readNullable[Double] and
            (JsPath \ "salaryType").read[SalaryType] and
            (JsPath \ "salaryMin").readNullable[Double] and
            (JsPath \ "salaryMax").readNullable[Double] and
            (JsPath \ "currency").read[CurrencyType] and
            (JsPath \ "taxTerm").read[TaxTermType] and
            (JsPath \ "paymentTerm").read[PaymentTermType] and
            (JsPath \ "backgroundCheck").read[BackgroundCheckType]
    )(JobRequirementXtnDTO)
    
    def apply(jobRequirementXtn: JobRequirementXtn) = new JobRequirementXtnDTO(jobRequirementXtn)
}
