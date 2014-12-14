package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._

object UserSkillRepository {
  
  val query = TableQuery[UserSkills]
  
  def create(userSkill: UserSkill) = {
    DB.withSession {
       implicit session: Session =>
         query.insert(userSkill)
    }
  }
  
  def udate(userSkill: UserSkill) = {
    DB.withSession {
       implicit session: Session =>
         query.filter( u => u.userId === userSkill.userId && u.skillId === userSkill.skillId).update(userSkill)
    }
  }
  
  def find(userId: Long, skillId: Long): Option[UserSkill] = {
    DB.withSession {
      implicit session =>
        query.filter(s => s.userId === userId && s.skillId === skillId).firstOption
    }
  }
    
  def findAll(userId: Long): Seq[UserSkillDTO] = {
    DB.withSession {
      implicit session =>
        //query.filter(_.userId === userId).list
        val q = for {
            u <- query
            s <- u.skill
        } yield (u.userId, u.skillId, u.skillLevel, u.descriptionShort, u.descriptionLong, s.name)
         
        q.list.map{case (userId, skillId, skillLLevel, descriptionShort, descriptionLong, skillName) 
                => UserSkillDTO(userId, skillId, skillLLevel, descriptionShort, descriptionLong, skillName)}
    }
  }
  
  def delete(userId: Long, skillId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter( u => u.userId === userId && u.skillId === skillId).delete
    }
  }
}

