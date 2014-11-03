package models.repositories

import models.tables._
import models.dtos._

import org.specs2.mutable.Specification
import play.api.test.WithApplication
import play.api.db.slick.DB
import models._
import models.dtos._
import models.repositories._
import play.api.db.slick.Config.driver.simple._

class UserRepositoryTest extends Specification {

  "User Repository" should {

    "save and query users" in new WithApplication {
      DB.withSession {
          implicit session: Session =>
            session.withTransaction{
              val country = Country(None, "usa", "USA", true)
              val countryId = CountryRepository.create(country)
              
              val user = User(None, "Krzysztof", "Nowak", "test@email.com", "assa", countryId, "token", true)
              val userId = UserRepository create user
              val userOpt = UserRepository findByEmail "test@email.com"
    
              userOpt.map(_.email) must beSome(user.email)
              userOpt.map(_.firstName) must beSome(user.firstName)
              userOpt.map(_.lastName) must beSome(user.lastName)
              userOpt.map(_.userId) must not be_=== None
              
              session.rollback
            }
      }
    }
  }
}