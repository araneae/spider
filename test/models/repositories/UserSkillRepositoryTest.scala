package models.repositories

import models.tables._
import models.dtos._

import org.specs2.mutable.Specification
import play.api.test.WithApplication
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
              val industry = Industry(None, "Software", "-software-", "This is for Software Industry")
              val industryId = IndustryRepository.create(industry)
              
              // create a skill
              val skill = Skill(None, industryId, "Software", "-software-", "This is for Software Industry")
              val skillId = SkillRepository.create(skill)
              
              // create a user
              val user = User(None, "Krzysztof", "Nowak", "test@email.com", "assa")
              val userId = UserRepository create user
              
              
              val userSkill = UserSkill(userId, skillId, SkillLevel.EXPERT, "Good at software", "I done many software projects.")
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