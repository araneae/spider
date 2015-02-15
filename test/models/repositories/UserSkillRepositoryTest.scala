package models.repositories

import models.tables._
import models.dtos._

import org.specs2.mutable.Specification
import play.api.test.WithApplication
import org.joda.time.DateTime
import play.api.db.slick.DB
import models._
import play.api.db.slick.Config.driver.simple._
import enums._

class UserSkillRepositoryTest extends Specification {

  "UserSkill Repository" should {

    "save and query UserSkill" in new WithApplication {
      DB.withSession {
          implicit session: Session =>
            session.withTransaction{ 
              // create an industry
              val industry = Industry(None, "Software", "This is for Software Industry", 1)
              val industryId = IndustryRepository.create(industry)
              val country = Country(None, "usa", "usa", true)
              val countryId = CountryRepository.create(country)
              
              // create a skill
              val skill = Skill(None, industryId, "Software", "This is for Software Industry", 1)
              val skillId = SkillRepository.create(skill)
              
              // create a user
              val user = User(None, "Krzysztof", None, "Nowak", "test@email.com", None, "assa", countryId, "token", true,
                                      new DateTime, UserStatusType.ACTIVE, None)
              val userId = UserRepository create user
              
              
              val userSkill = UserSkill(userId, skillId, SkillLevel.EXPERT, Some("Good at software"), Some("I done many software projects."), 1)
              UserSkillRepository.create(userSkill)
              
              
              val userSkillOpt = UserSkillRepository find(userId, skillId)
    
              userSkillOpt.map(_.userId) must beSome(userSkill.userId)
              userSkillOpt.map(_.skillId) must beSome(userSkill.skillId)
              userSkillOpt.map(_.skillLevel) must beSome(userSkill.skillLevel)
              userSkillOpt.map(_.descriptionShort) must beSome(userSkill.descriptionShort)
              userSkillOpt.map(_.descriptionLong) must beSome(userSkill.descriptionLong)
            
              session.rollback
          }
      }
    }
  }
}