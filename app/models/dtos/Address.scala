package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

case class Address(
                address1: Option[String],
                address2: Option[String],
                city: Option[String],
                zip: Option[String],
                stateId: Long,
                countryId: Long)

object Address extends Function6[Option[String], Option[String], Option[String], Option[String], Long, Long, Address]
{
    implicit val addressWrites : Writes[Address] = (
            (JsPath \ "address1").write[Option[String]] and
            (JsPath \ "address2").write[Option[String]] and
            (JsPath \ "city").write[Option[String]] and
            (JsPath \ "zip").write[Option[String]] and
            (JsPath \ "stateId").write[Long] and
            (JsPath \ "countryId").write[Long]
    )(unlift(Address.unapply))
      
    implicit val addressReads : Reads[Address] = (
          (JsPath \ "address1").readNullable[String] and
          (JsPath \ "address2").readNullable[String] and
          (JsPath \ "city").readNullable[String] and
          (JsPath \ "zip").readNullable[String] and
          (JsPath \ "stateId").read[Long] and
          (JsPath \ "countryId").read[Long]
    )(Address)
}
