package models.repositories

import models.tables._
import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.dtos._

object SkillRepository {
  
  val query = TableQuery[Skills]
  
  def create(skill: Skill) :Long= {
    DB.withSession {
       implicit session: Session =>
         query returning query.map(_.skillId) += skill
    }
  }
  
  def udate(skill: Skill) = {
    DB.withSession {
       implicit session: Session =>
         query.filter(_.skillId === skill.skillId.get).update(skill)
    }
  }
  
  def find(skillId: Long): Option[Skill] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.skillId === skillId) firstOption
    }
  }
    
  def findAll(): Seq[SkillDTO] = {
    DB.withSession {
       implicit session: Session =>
       val q = for {
           s <- query
           i <- s.industry
         } yield (s.skillId, s.industryId, s.name, s.description, i.name)
         
       q.list.map{case (skillId, industryId, name, description, industryName) => SkillDTO(skillId, industryId, name, description, industryName)}
    }
  }
  
  def delete(skillId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter(_.skillId === skillId).delete
    }
  }
}

