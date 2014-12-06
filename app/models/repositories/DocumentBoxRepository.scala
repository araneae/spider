package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import enums.DocumentType._

object DocumentBoxRepository {
  
  val query = TableQuery[DocumentBoxes]
  
  def create(documentBox: DocumentBox) = {
    DB.withSession {
       implicit session: Session =>
         query returning query.map(_.documentBoxId) += documentBox
    }
  }
  
  def find(documentBoxId: Long): Option[DocumentBox] = {
    DB.withSession {
      implicit session =>
        query.filter(d => d.documentBoxId === documentBoxId).firstOption
    }
  }
  
  def udate(documentBox: DocumentBox) = {
    DB.withSession {
       implicit session: Session =>
         query filter(_.documentBoxId === documentBox.documentBoxId.get) update documentBox 
    }
  }
  
  def delete(documentBoxId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter( u => u.documentBoxId === documentBoxId).delete
    }
  }
}

