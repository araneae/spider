package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class JobTitleFull(
                   jobTitleId: Long,
                   name: String,
                   code: String,
                   description: Option[String])

object JobTitleFull extends Function4[Long, String, String, Option[String], JobTitleFull]
{
    implicit val skillWrites : Writes[JobTitleFull] = (
            (JsPath \ "jobTitleId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "code").write[String] and
            (JsPath \ "description").write[Option[String]]
    )(unlift(JobTitleFull.unapply))
      
    implicit val skillReads : Reads[JobTitleFull] = (
          (JsPath \ "jobTitleId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "code").read[String] and
          (JsPath \ "description").readNullable[String]
    )(JobTitleFull)
    
}