package models.repositories

import models.tables._
import models.dtos._

import org.specs2.mutable.Specification
import play.api.test.WithApplication
import play.api.db.slick.DB
import models._
import play.api.db.slick.Config.driver.simple._

class SkillRepositoryTest extends Specification {

  "Skill Repository" should {

    "save and query Skill" in new WithApplication {
      DB.withSession {
          implicit session: Session =>
            session.withTransaction{
              val industry = Industry(None, "Software", "-software-", "This is for Software Industry", 1)
              val industryId = IndustryRepository.create(industry)
              
              val skill = Skill(None, industryId, "Software", "-software-", "This is for Software Industry", 1)
              
              val skillId = SkillRepository.create(skill)
              val skillOpt = SkillRepository find skillId
    
              skillOpt.map(_.industryId) must beSome(skill.industryId)
              skillOpt.map(_.name) must beSome(skill.name)
              skillOpt.map(_.code) must beSome(skill.code)
              skillOpt.map(_.description) must beSome(skill.description)
              skillOpt.map(_.skillId) must not be_=== None
              
              session.rollback
            }
      }
    }
  }
}