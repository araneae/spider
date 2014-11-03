package models.repositories

import org.specs2.mutable.Specification

import models.tables._
import models.dtos._
import play.api.db.slick.Config.driver.simple.Session
import play.api.db.slick.DB
import play.api.test.WithApplication

class DomainRepositoryTest extends Specification {

  "Domain Repository" should {

    "save and query Domain" in new WithApplication {
      DB.withSession{
          implicit session: Session =>
            session.withTransaction{
              val industry = Industry(None, "Software", "-software-", Some("This is for Software Industry"), 1)
              val industryId = IndustryRepository.create(industry)
              
              val domain = Domain(None, industryId, "Software", "-software-", Some("This is for Software Industry"), 1)
              
              val domainId = DomainRepository.create(domain)
              val domainOpt = DomainRepository find domainId
    
              domainOpt.map(_.industryId) must beSome(domain.industryId)
              domainOpt.map(_.name) must beSome(domain.name)
              domainOpt.map(_.code) must beSome(domain.code)
              domainOpt.map(_.description) must beSome(domain.description)
              domainOpt.map(_.domainId) must not be_=== None
              
              session.rollback
            }
      }
    }
  }
}