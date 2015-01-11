package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.PaymentTermType._
import enums.CurrencyType._
import enums.TaxTermType._
import enums.SalaryType._
import enums.BackgroundCheckType._

/**
 * Extension table to define job requirements
 *  
 */
case class JobRequirementXtn(
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
                 backgroundCheck: BackgroundCheckType,
                 createdUserId: Long,
                 createdAt: DateTime = new DateTime(),
                 updatedUserId: Option[Long] = None,
                 updatedAt: Option[DateTime] = None) {
   def this(jobRequirementXtnDTO: JobRequirementXtnDTO,
            createdUserId: Long, 
            createdAt: DateTime, 
            updatedUserId: Option[Long], 
            updatedAt: Option[DateTime]) {
       this(jobRequirementXtnDTO.jobRequirementXtnId,
           jobRequirementXtnDTO.targetStartDate,
           jobRequirementXtnDTO.targetEndDate,
           jobRequirementXtnDTO.locationLat,
           jobRequirementXtnDTO.locationLng,
           jobRequirementXtnDTO.salaryType,
           jobRequirementXtnDTO.salaryMin,
           jobRequirementXtnDTO.salaryMax,
           jobRequirementXtnDTO.currency,
           jobRequirementXtnDTO.taxTerm,
           jobRequirementXtnDTO.paymentTerm,
           jobRequirementXtnDTO.backgroundCheck,
           createdUserId, 
           createdAt, 
           updatedUserId, 
           updatedAt)
   }
}

object JobRequirementXtn extends Function16[Option[Long],
                 Option[DateTime],
                 Option[DateTime],
                 Option[Double],
                 Option[Double],
                 SalaryType,
                 Option[Double],
                 Option[Double],
                 CurrencyType,
                 TaxTermType,
                 PaymentTermType,
                 BackgroundCheckType,
                 Long,
                 DateTime,
                 Option[Long],
                 Option[DateTime], JobRequirementXtn]
{
    implicit val jobRequirementXtnWrites : Writes[JobRequirementXtn] = (
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
            (JsPath \ "backgroundCheck").write[BackgroundCheckType] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(JobRequirementXtn.unapply))
      
    implicit val jobRequirementXtnReads : Reads[JobRequirementXtn] = (
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
            (JsPath \ "backgroundCheck").read[BackgroundCheckType] and
            (JsPath \ "createdUserId").read[Long] and
            (JsPath \ "createdAt").read[DateTime] and
            (JsPath \ "updatedUserId").readNullable[Long] and
            (JsPath \ "updatedAt").readNullable[DateTime]
    )(JobRequirementXtn)
    
    def apply(jobRequirementXtnDTO: JobRequirementXtnDTO,
              createdUserId: Long,
              createdAt: DateTime,
              updatedUserId: Option[Long],
              updatedAt: Option[DateTime]) = new JobRequirementXtn(jobRequirementXtnDTO, createdUserId, createdAt, updatedUserId, updatedAt)
}
