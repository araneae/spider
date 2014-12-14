package models.dtos

import enums.SkillLevel._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums._

case class UserSkillDTO(userId: Long,
                skillId: Long,
                skillLevel: SkillLevel,
                descriptionShort: Option[String],
                descriptionLong: Option[String],
                skillName: String
                )

object UserSkillDTO extends Function6[Long, Long, SkillLevel, Option[String], Option[String], String, UserSkillDTO]
{
    implicit val enumTypeFormat = EnumUtils.enumFormat(SkillLevel)
  
    implicit val userSkillWrites : Writes[UserSkillDTO] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "skillId").write[Long] and
            (JsPath \ "skillLevel").write[SkillLevel] and
            (JsPath \ "descriptionShort").write[Option[String]] and
            (JsPath \ "descriptionLong").write[Option[String]] and
            (JsPath \ "skillName").write[String]
    )(unlift(UserSkillDTO.unapply))
      
    implicit val userSkillReads : Reads[UserSkillDTO] = (
          (JsPath \ "userId").read[Long] and
          (JsPath \ "skillId").read[Long] and
          (JsPath \ "skillLevel").read[SkillLevel] and
          (JsPath \ "descriptionShort").readNullable[String] and
          (JsPath \ "descriptionLong").readNullable[String] and
          (JsPath \ "skillName").read[String]
    )(UserSkillDTO)
}
/*
https://groups.google.com/forum/#!topic/play-framework/ENlcpDzLZo8
                
Here is how you SHALL write it:

This COMPILES

    import play.api.libs.json._
    import play.api.libs.functional.syntax._

    case class Person(name: String, age: Long)
    object Person {
      implicit val personReads = (
        (__ \ 'name).read[String] and
        (__ \ 'age).read[Long]
      )(Person.apply _)
    }

This COMPILES

    import play.api.libs.json._
    import play.api.libs.functional.syntax._

    case class Person(name: String, age: Long)
    object Person extends Function2[String, Long, Person]{
      implicit val personReads = (
        (__ \ 'name).read[String] and
        (__ \ 'age).read[Long]
      )(Person)
    }

This COMPILES

    import play.api.libs.json._
    import play.api.libs.functional.syntax._

    case class Person(name: String, age: Long)
    object Person extends Function2[String, Long, Person]{
      implicit val personReads = Json.reads[Person]
    }
*/                