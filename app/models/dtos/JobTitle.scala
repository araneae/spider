package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.EmploymentType._
import enums.JobStatusType._
import enums.SalaryTermType._
import enums.CurrencyType._

/**
 * Defines job titles
 *  
 */
case class JobTitle(  
                 jobTitleId: Option[Long],
                 companyId: Long,
                 name: String,
                 code: String,
                 description: Option[String],
                 createdUserId: Long,
                 createdAt: DateTime = new DateTime(),
                 updatedUserId: Option[Long] = None,
                 updatedAt: Option[DateTime] = None)

object JobTitle extends Function9[Option[Long], Long, String, String, Option[String], Long, DateTime, Option[Long], Option[DateTime], JobTitle]
{
    implicit val jobTitleWrites : Writes[JobTitle] = (
            (JsPath \ "jobTitleId").write[Option[Long]] and
            (JsPath \ "companyId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "code").write[String] and
            (JsPath \ "description").write[Option[String]] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(JobTitle.unapply))
      
    implicit val jobTitleReads : Reads[JobTitle] = (
          (JsPath \ "jobTitleId").readNullable[Long] and
          (JsPath \ "companyId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "code").read[String] and
          (JsPath \ "description").readNullable[String] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(JobTitle)
    
}
