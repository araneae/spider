package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime
import enums.CompanyStatusType._

/**
 * Defines company
 *  
 */
case class Company(  
                 companyId: Option[Long],
                 name: String,
                 status: CompanyStatusType,
                 address: String,
                 email: String,
                 website: Option[String],
                 telephone: String,
                 createdUserId: Long,
                 createdAt: DateTime = new DateTime(),
                 updatedUserId: Option[Long] = None,
                 updatedAt: Option[DateTime] = None) {
  def this(companyDTO: CompanyDTO, 
          createdUserId: Long, 
          createdAt: DateTime,
          updatedUserId: Option[Long], 
          updatedAt: Option[DateTime]) {
      this(companyDTO.companyId,
           companyDTO.name,
           companyDTO.status,
           companyDTO.address,
           companyDTO.email,
           companyDTO.website,
           companyDTO.telephone,
           createdUserId,
           createdAt,
           updatedUserId,
           updatedAt)
  }
}

object Company extends Function11[Option[Long], String, CompanyStatusType, String, String, Option[String], String, Long, DateTime, Option[Long], Option[DateTime], Company]
{
    implicit val companyWrites : Writes[Company] = (
            (JsPath \ "companyId").write[Option[Long]] and
            (JsPath \ "name").write[String] and
            (JsPath \ "status").write[CompanyStatusType] and
            (JsPath \ "address").write[String] and
            (JsPath \ "email").write[String] and
            (JsPath \ "website").write[Option[String]] and
            (JsPath \ "telephone").write[String] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(Company.unapply))
      
    implicit val companyReads : Reads[Company] = (
            (JsPath \ "companyId").readNullable[Long] and
            (JsPath \ "name").read[String] and
            (JsPath \ "status").read[CompanyStatusType] and
            (JsPath \ "address").read[String] and
            (JsPath \ "email").read[String] and
            (JsPath \ "website").readNullable[String] and
            (JsPath \ "telephone").read[String] and
            (JsPath \ "createdUserId").read[Long] and
            (JsPath \ "createdAt").read[DateTime] and
            (JsPath \ "updatedUserId").readNullable[Long] and
            (JsPath \ "updatedAt").readNullable[DateTime]
    )(Company)
    
    def apply(companyDTO: CompanyDTO,
          createdUserId: Long, 
          createdAt: DateTime, 
          updatedUserId: Option[Long], 
          updatedAt: Option[DateTime]) = new Company(companyDTO, createdUserId, createdAt, updatedUserId, updatedAt)
}
