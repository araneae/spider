package models.repositories

import models.tables._
import models.dtos._

import org.specs2.mutable.Specification
import play.api.test.WithApplication
import play.api.db.slick.DB
import models._
import play.api.db.slick.Config.driver.simple._

class IndustryRepositoryTest extends Specification {

  "Industry Repository" should {

    "save and query Industry" in new WithApplication {
      DB.withSession {
          implicit session: Session =>
            session.withTransaction{
              val industry = Industry(None, "Software", "-software-", "This is for Software Industry")
              val industryId = IndustryRepository.create(industry)
              
              val industryOpt = IndustryRepository find industryId
    
              industryOpt.map(_.name) must beSome(industry.name)
              industryOpt.map(_.code) must beSome(industry.code)
              industryOpt.map(_.description) must beSome(industry.description)
              industryOpt.map(_.industryId) must not be_=== None
              
              session.rollback
            }
      }
    }
  }
}