package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._

object DocumentTagRepository {
  
  val query = TableQuery[DocumentTags]
  
  def create(documentTag: DocumentTag) = {
    DB.withSession {
       implicit session: Session =>
         query insert documentTag
    }
  }
  
  def findAll(userId: Long, documentId: Long): Seq[DocumentTag] = {
    DB.withSession {
      implicit session =>
       query.filter( d => d.userId === userId &&
                          d.documentId === documentId).list
    }
  }
  
  def findByUserTagId(userId: Long, userTagId: Long): Seq[DocumentTag] = {
    DB.withSession {
      implicit session =>
       query.filter( d => d.userId === userId &&
                          d.userTagId === userTagId).list
    }
  }
  
  def findDocumentByUserTagId(userId: Long, userTagId: Long): Seq[Document] = {
    DB.withSession {
      implicit session =>
       val q = for {
            docTag <- query.filter( d => d.userId === userId &&
                               d.userTagId === userTagId)
            doc <- docTag.document
        } yield (doc)
         
        q.list
    }
  }
  
  def delete(userId: Long, documentId: Long, userTagId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter( d => d.userId === userId &&
                             d.userTagId === userTagId &&
                             d.documentId === documentId).delete
    }
  }
}

