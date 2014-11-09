package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * List of countries
 */
case class Country(countryId: Option[Long],
                  code: String,
                  name: String,
                  active: Boolean)

object Country extends Function4[Option[Long], String, String, Boolean, Country]
{
    implicit val countryWrites : Writes[Country] = (
            (JsPath \ "countryId").write[Option[Long]] and
            (JsPath \ "code").write[String] and
            (JsPath \ "name").write[String] and
            (JsPath \ "active").write[Boolean]
    )(unlift(Country.unapply))
      
    implicit val countryReads : Reads[Country] = (
          (JsPath \ "countryId").readNullable[Long] and
          (JsPath \ "code").read[String] and
          (JsPath \ "name").read[String] and
          (JsPath \ "active").read[Boolean]
    )(Country)
}
