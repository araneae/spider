package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.CompanyStatusType._

/**
 * Defines company DTO
 *  
 */
case class CompanyDTO(  
                 companyId: Option[Long],
                 name: String,
                 status: CompanyStatusType,
                 address: String,
                 email: String,
                 website: Option[String],
                 telephone: String) {
  def this(company: Company) {
      this(company.companyId,
           company.name,
           company.status,
           company.address,
           company.email,
           company.website,
           company.telephone)
  }
}

object CompanyDTO extends Function7[Option[Long], String, CompanyStatusType, String, String, Option[String], String, CompanyDTO]
{
    implicit val companyWrites : Writes[CompanyDTO] = (
            (JsPath \ "companyId").write[Option[Long]] and
            (JsPath \ "name").write[String] and
            (JsPath \ "status").write[CompanyStatusType] and
            (JsPath \ "address").write[String] and
            (JsPath \ "email").write[String] and
            (JsPath \ "website").write[Option[String]] and
            (JsPath \ "telephone").write[String]
    )(unlift(CompanyDTO.unapply))
      
    implicit val companyReads : Reads[CompanyDTO] = (
            (JsPath \ "companyId").readNullable[Long] and
            (JsPath \ "name").read[String] and
            (JsPath \ "status").read[CompanyStatusType] and
            (JsPath \ "address").read[String] and
            (JsPath \ "email").read[String] and
            (JsPath \ "website").readNullable[String] and
            (JsPath \ "telephone").read[String]
    )(CompanyDTO)
    
    def apply(company: Company) = new CompanyDTO(company)
}
