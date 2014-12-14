package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * Defines job titles
 *  
 */
case class JobTitle(  
                 jobTitleId: Option[Long],
                 companyId: Long,
                 name: String,
                 description: String,
                 createdUserId: Long,
                 createdAt: DateTime = new DateTime(),
                 updatedUserId: Option[Long] = None,
                 updatedAt: Option[DateTime] = None) {
   def this(jobTitleDTO: JobTitleDTO,
            createdUserId: Long, 
            createdAt: DateTime, 
            updatedUserId: Option[Long], 
            updatedAt: Option[DateTime]) {
       this(jobTitleDTO.jobTitleId,
            jobTitleDTO.companyId,
            jobTitleDTO.name,
            jobTitleDTO.description,
            createdUserId, 
            createdAt, 
            updatedUserId, 
            updatedAt)
   }
}

object JobTitle extends Function8[Option[Long], Long, String, String, Long, DateTime, Option[Long], Option[DateTime], JobTitle]
{
    implicit val jobTitleWrites : Writes[JobTitle] = (
            (JsPath \ "jobTitleId").write[Option[Long]] and
            (JsPath \ "companyId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "description").write[String] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(JobTitle.unapply))
      
    implicit val jobTitleReads : Reads[JobTitle] = (
          (JsPath \ "jobTitleId").readNullable[Long] and
          (JsPath \ "companyId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "description").read[String] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(JobTitle)
    
    def apply(jobTitleDTO: JobTitleDTO,
            createdUserId: Long, 
            createdAt: DateTime, 
            updatedUserId: Option[Long], 
            updatedAt: Option[DateTime]) = new JobTitle(jobTitleDTO, createdUserId, createdAt, updatedUserId, updatedAt)
}
