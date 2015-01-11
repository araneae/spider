package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.EmploymentType._
import enums.JobStatusType._
import enums.CurrencyType._

/**
 * Defines job requirements
 *  
 */
case class JobApplication(  
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
                 postDate: Option[DateTime],
                 jobApplicationXtnId: Long,
                 createdUserId: Long,
                 createdAt: DateTime = new DateTime(),
                 updatedUserId: Option[Long] = None,
                 updatedAt: Option[DateTime] = None) {
   def this(jobApplicationDTO: JobApplicationDTO,
            jobApplicationXtnId: Long,
            createdUserId: Long, 
            createdAt: DateTime, 
            updatedUserId: Option[Long], 
            updatedAt: Option[DateTime]) {
       this(jobApplicationDTO.jobApplicationId,
           jobApplicationDTO.companyId,
           jobApplicationDTO.code,
           jobApplicationDTO.refNumber,
           jobApplicationDTO.title,
           jobApplicationDTO.employmentType,
           jobApplicationDTO.industryId,
           jobApplicationDTO.location,
           jobApplicationDTO.description,
           jobApplicationDTO.status,
           jobApplicationDTO.positions,
           jobApplicationDTO.jobTitleId,
           jobApplicationDTO.postDate,
           jobApplicationXtnId,
           createdUserId, 
           createdAt, 
           updatedUserId, 
           updatedAt)
   }
}

object JobApplication extends Function18[Option[Long], Long, String, Option[String], String, EmploymentType, Long, String,
                            String, JobStatusType, Int, Long, Option[DateTime], Long, Long, DateTime, Option[Long], Option[DateTime], JobApplication]
{
    implicit val jobApplicationWrites : Writes[JobApplication] = (
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
            (JsPath \ "postDate").write[Option[DateTime]] and
            (JsPath \ "jobApplicationXtnId").write[Long] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(JobApplication.unapply))
      
    implicit val jobApplicationReads : Reads[JobApplication] = (
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
          (JsPath \ "postDate").readNullable[DateTime] and
          (JsPath \ "jobApplicationXtnId").read[Long] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(JobApplication)
    
    def apply(jobApplicationDTO: JobApplicationDTO,
              jobApplicationXtnId: Long,
              createdUserId: Long,
              createdAt: DateTime,
              updatedUserId: Option[Long],
              updatedAt: Option[DateTime]) = new JobApplication(jobApplicationDTO, jobApplicationXtnId, createdUserId, createdAt, updatedUserId, updatedAt)
}
