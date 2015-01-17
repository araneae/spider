package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.RelocationType._
import enums.TravelingType._

/**
 * Defines job application
 *  
 */
case class JobApplication(
                 jobApplicationId: Option[Long],
                 companyId: Long,
                 jobRequirementId: Long,
                 phone: String,
                 availableInWeeks: Int,
                 relocation: RelocationType,
                 traveling: TravelingType,
                 message: String,
                 createdUserId: Long,
                 createdAt: DateTime = new DateTime(),
                 updatedUserId: Option[Long] = None,
                 updatedAt: Option[DateTime] = None) {
   def this(jobApplicationDTO: JobApplicationDTO,
            createdUserId: Long, 
            createdAt: DateTime, 
            updatedUserId: Option[Long], 
            updatedAt: Option[DateTime]) {
       this(jobApplicationDTO.jobApplicationId,
           jobApplicationDTO.companyId,
           jobApplicationDTO.jobRequirementId,
           jobApplicationDTO.phone,
           jobApplicationDTO.availableInWeeks,
           jobApplicationDTO.relocation,
           jobApplicationDTO.traveling,
           jobApplicationDTO.message,
           createdUserId, 
           createdAt, 
           updatedUserId, 
           updatedAt)
   }
}

object JobApplication extends Function12[Option[Long], Long, Long, String, Int, RelocationType, TravelingType, String, Long, DateTime, Option[Long], Option[DateTime], JobApplication]
{
    implicit val jobApplicationWrites : Writes[JobApplication] = (
            (JsPath \ "jobApplicationId").write[Option[Long]] and
            (JsPath \ "companyId").write[Long] and
            (JsPath \ "jobRequirementId").write[Long] and
            (JsPath \ "phone").write[String] and
            (JsPath \ "availableInWeeks").write[Int] and
            (JsPath \ "relocation").write[RelocationType] and
            (JsPath \ "traveling").write[TravelingType] and
            (JsPath \ "message").write[String] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(JobApplication.unapply))
      
    implicit val jobApplicationReads : Reads[JobApplication] = (
          (JsPath \ "jobApplicationId").readNullable[Long] and
          (JsPath \ "companyId").read[Long] and
          (JsPath \ "jobRequirementId").read[Long] and
          (JsPath \ "phone").read[String] and
          (JsPath \ "availableInWeeks").read[Int] and
          (JsPath \ "relocation").read[RelocationType] and
          (JsPath \ "traveling").read[TravelingType] and
          (JsPath \ "message").read[String] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(JobApplication)
    
    def apply(jobApplicationDTO: JobApplicationDTO,
              createdUserId: Long,
              createdAt: DateTime,
              updatedUserId: Option[Long],
              updatedAt: Option[DateTime]) = new JobApplication(jobApplicationDTO, createdUserId, createdAt, updatedUserId, updatedAt)
}
