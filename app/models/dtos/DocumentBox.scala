package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * DocumentBox.
 * 
 */
case class DocumentBox(
                   documentBoxId: Option[Long],
                   name: String,
                   description: Option[String],
                   default: Boolean,
                   createdUserId: Long,
                   createdAt: DateTime = new DateTime(),
                   updatedUserId: Option[Long] = None,
                   updatedAt: Option[DateTime] = None)

object DocumentBox extends Function8[Option[Long], String, Option[String], Boolean, Long, DateTime, Option[Long], Option[DateTime], DocumentBox]
{
    implicit val documentBoxWrites : Writes[DocumentBox] = (
            (JsPath \ "documentBoxId").write[Option[Long]] and
            (JsPath \ "name").write[String] and
            (JsPath \ "description").write[Option[String]] and
            (JsPath \ "default").write[Boolean] and
            (JsPath \ "createdUserId").write[Long] and
            (JsPath \ "createdAt").write[DateTime] and
            (JsPath \ "updatedUserId").write[Option[Long]] and
            (JsPath \ "updatedAt").write[Option[DateTime]]
    )(unlift(DocumentBox.unapply))

    implicit val documentBoxReads : Reads[DocumentBox] = (
          (JsPath \ "documentBoxId").readNullable[Long] and
          (JsPath \ "name").read[String] and
          (JsPath \ "description").readNullable[String] and
          (JsPath \ "default").read[Boolean] and
          (JsPath \ "createdUserId").read[Long] and
          (JsPath \ "createdAt").read[DateTime] and
          (JsPath \ "updatedUserId").readNullable[Long] and
          (JsPath \ "updatedAt").readNullable[DateTime]
    )(DocumentBox)
}
