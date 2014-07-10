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
         query returning query.map(_.id) += skill
    }
  }
  
  def udate(skill: Skill) = {
    DB.withSession {
       implicit session: Session =>
         query.filter(_.id === skill.id).update(skill)
    }
  }
  
  def find(id: Long): Option[Skill] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.id === id) firstOption
    }
  }
  
  def findByCode(code: String): Option[Skill] = {
    DB.withSession {
       implicit session: Session =>
          query filter(_.code === code) firstOption
    }
  }
  
  def findAll(): Seq[SkillFull] = {
    DB.withSession {
       implicit session: Session =>
       val q = for {
           s <- query
           i <- s.industry
         } yield (s.id, s.industryId, s.name, s.code, s.description, i.name)
         
       q.list.map{case (id, industryId, name, code, description, industryName) => SkillFull(id, industryId, name, code, description, industryName)}
    }
  }
  
  def delete(id: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter(_.id === id).delete
    }
  }
}

