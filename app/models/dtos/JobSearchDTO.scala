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
 * Defines job search criteria
 *  
 */
case class JobSearchDTO(  
                 title: Option[String],
                 location: String,
                 locationLat: Option[Double],
                 locationLng: Option[Double],
                 distance: Double,
                 company: Option[String],
                 contents: Option[String],
                 date: DateTime)

object JobSearchDTO extends Function8[Option[String], String, Option[Double], Option[Double], Double, Option[String], Option[String], DateTime, JobSearchDTO]
{
    implicit val jobSearchWrites : Writes[JobSearchDTO] = (
            (JsPath \ "title").write[Option[String]] and
            (JsPath \ "location").write[String] and
            (JsPath \ "locationLat").write[Option[Double]] and
            (JsPath \ "locationLng").write[Option[Double]] and
            (JsPath \ "distance").write[Double] and
            (JsPath \ "company").write[Option[String]] and
            (JsPath \ "contents").write[Option[String]] and
            (JsPath \ "date").write[DateTime]
    )(unlift(JobSearchDTO.unapply))
      
    implicit val jobSearchReads : Reads[JobSearchDTO] = (
          (JsPath \ "title").readNullable[String] and
          (JsPath \ "location").read[String] and
          (JsPath \ "locationLat").readNullable[Double] and
          (JsPath \ "locationLng").readNullable[Double] and
          (JsPath \ "distance").read[Double] and
          (JsPath \ "company").readNullable[String] and
          (JsPath \ "contents").readNullable[String] and
          (JsPath \ "date").read[DateTime]
    )(JobSearchDTO)
}
