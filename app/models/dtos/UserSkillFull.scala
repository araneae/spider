package models.dtos

import enums.SkillLevel._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums._

case class UserSkillFull(userId: Long,
                skillId: Long,
                skillLevel: SkillLevel,
                descriptionShort: String,
                descriptionLong: String,
                skillName: String
                )

object UserSkillFull extends Function6[Long, Long, SkillLevel, String, String, String, UserSkillFull]
{
    implicit val enumTypeFormat = EnumUtils.enumFormat(SkillLevel)
  
    implicit val userSkillWrites : Writes[UserSkillFull] = (
            (JsPath \ "userId").write[Long] and
            (JsPath \ "skillId").write[Long] and
            (JsPath \ "skillLevel").write[SkillLevel] and
            (JsPath \ "descriptionShort").write[String] and
            (JsPath \ "descriptionLong").write[String] and
            (JsPath \ "skillName").write[String]
    )(unlift(UserSkillFull.unapply))
      
    implicit val userSkillReads : Reads[UserSkillFull] = (
          (JsPath \ "userId").read[Long] and
          (JsPath \ "skillId").read[Long] and
          (JsPath \ "skillLevel").read[SkillLevel] and
          (JsPath \ "descriptionShort").read[String] and
          (JsPath \ "descriptionLong").read[String] and
          (JsPath \ "skillName").read[String]
    )(UserSkillFull)
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