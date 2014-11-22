package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.EmploymentType._
import enums.JobStatusType._
import enums.SalarayTermType._
import enums.CurrencyType._

/**
 * Defines a job requirement
 *  
 */
case class Job(  jobId: Option[Long],
                 userId: Long,
                 code: String,
                 refNumber: Option[String],
                 title: String,
                 employmentType: EmploymentType,
                 industryId: Long,
                 location: String,
                 salaray: Double,
                 currencyType: CurrencyType,
                 salarayTerm: SalarayTermType,
                 description: String,
                 searchTerms: String,
                 jobStatusType: JobStatusType,
                 createdUserId: Long,
                 createdAt: DateTime = new DateTime(),
                 updatedUserId: Option[Long] = None,
                 updatedAt: Option[DateTime] = None)

object Job extends Function18[Option[Long], Long, String, Option[String], String, EmploymentType, Long, String, Double, CurrencyType, 
                                SalarayTermType, String, String, JobStatusType, Long, DateTime, Option[Long], Option[DateTime], Job]
{
    implicit val jobWrites : Writes[Job] = (
            (JsPath \ "jobId").write[Option[Long]] and
            (JsPath \ "userId").write[Long] and
            (JsPath \ "code").write[String] and
            (JsPath \ "refNumber").write[Option[String]] and
            (JsPath \ "title").write[String] and
            (JsPath \ "employmentType").write[EmploymentType] and
            (JsPath \ "industryId").write[Long] and
            (JsPath \ "location").write[String] and
            (JsPath \ "salaray").write[Double] and
            (JsPath \ "currencyType").write[CurrencyType] and
            (JsPath \ "salarayTerm").write[SalarayTermType] and
            (JsPath \ "description").write[String] and
            (JsPath \ "searchTerms").write[String] and
            (JsPath \ "jobStatusType").write[JobStatusType] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(Job.unapply))
      
    implicit val jobReads : Reads[Job] = (
          (JsPath \ "jobId").readNullable[Long] and
          (JsPath \ "userId").read[Long] and
          (JsPath \ "code").read[String] and
          (JsPath \ "refNumber").readNullable[String] and
          (JsPath \ "title").read[String] and
          (JsPath \ "employmentType").read[EmploymentType] and
          (JsPath \ "industryId").read[Long] and
          (JsPath \ "location").read[String] and
          (JsPath \ "salaray").read[Double] and
          (JsPath \ "currencyType").read[CurrencyType] and
          (JsPath \ "salarayTerm").read[SalarayTermType] and
          (JsPath \ "description").read[String] and
          (JsPath \ "searchTerms").read[String] and
          (JsPath \ "jobStatusType").read[JobStatusType] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(Job)
    
}
