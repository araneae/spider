package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import org.joda.time.DateTime

/**
 * List of states
 */
case class State(stateId: Option[Long],
                 code: String,
                 name: String,
                 countryId: Long)

object State extends Function4[Option[Long], String, String, Long, State]
{
    implicit val stateWrites : Writes[State] = (
            (JsPath \ "stateId").write[Option[Long]] and
            (JsPath \ "code").write[String] and
            (JsPath \ "name").write[String] and
            (JsPath \ "countryId").write[Long]
    )(unlift(State.unapply))
      
    implicit val stateReads : Reads[State] = (
          (JsPath \ "stateId").readNullable[Long] and
          (JsPath \ "code").read[String] and
          (JsPath \ "name").read[String] and
          (JsPath \ "countryId").read[Long]
    )(State)
}
