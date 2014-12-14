package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * Defines job title DTO
 *  
 */
case class JobTitleDTO(  
                 jobTitleId: Option[Long],
                 companyId: Long,
                 name: String,
                 description: String) {
  def this(jobTitle: JobTitle) {
      this (  jobTitle.jobTitleId,
              jobTitle.companyId,
              jobTitle.name,
              jobTitle.description)
  }
}

object JobTitleDTO extends Function4[Option[Long], Long, String, String, JobTitleDTO]
{
    implicit val jobTitleWrites : Writes[JobTitleDTO] = (
            (JsPath \ "jobTitleId").write[Option[Long]] and
            (JsPath \ "companyId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "description").write[String]
    )(unlift(JobTitleDTO.unapply))
      
    implicit val jobTitleReads : Reads[JobTitleDTO] = (
          (JsPath \ "jobTitleId").readNullable[Long] and
          (JsPath \ "companyId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "description").read[String]
    )(JobTitleDTO)
 
    def apply(jobTitle: JobTitle) =  new JobTitleDTO(jobTitle)
}
