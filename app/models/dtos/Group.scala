package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * A Group is a collection of contacts. A contact can be added in multiple groups.
 *  
 * How can a user the groups ?
 *    a) User can send the email to a group
 *    b) User can share a document to a group
 *    
 * 
 */
case class Group(  groupId: Option[Long],
                   userId: Long,
                   name: String,
                   description: String,
                   createdUserId: Long,
                   createdAt: DateTime = new DateTime(),
                   updatedUserId: Option[Long] = None,
                   updatedAt: Option[DateTime] = None)

object Group extends Function8[Option[Long], Long, String, String, Long, DateTime, Option[Long], Option[DateTime], Group]
{
    implicit val groupWrites : Writes[Group] = (
            (JsPath \ "groupId").write[Option[Long]] and
            (JsPath \ "userId").write[Long] and
            (JsPath \ "name").write[String] and
            (JsPath \ "description").write[String] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(Group.unapply))
      
    implicit val groupReads : Reads[Group] = (
          (JsPath \ "groupId").readNullable[Long] and
          (JsPath \ "userId").read[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "description").read[String] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(Group)
    
}
