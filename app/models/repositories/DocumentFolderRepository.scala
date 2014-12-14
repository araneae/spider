package models.repositories

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick.DB
import play.api.Play.current
import models.tables._
import models.dtos._
import enums.DocumentType._

object DocumentFolderRepository {
  
  val query = TableQuery[DocumentFolders]
  
  def create(documentFolder: DocumentFolder) = {
    DB.withSession {
       implicit session: Session =>
         query returning query.map(_.documentFolderId) += documentFolder
    }
  }
  
  def find(documentFolderId: Long): Option[DocumentFolder] = {
    DB.withSession {
      implicit session =>
        query.filter(d => d.documentFolderId === documentFolderId).firstOption
    }
  }
  
  def udate(documentFolder: DocumentFolder) = {
    DB.withSession {
       implicit session: Session =>
         query filter(_.documentFolderId === documentFolder.documentFolderId.get) update documentFolder 
    }
  }
  
  def delete(documentFolderId: Long) = {
    DB.withSession {
       implicit session: Session =>
          query.filter( u => u.documentFolderId === documentFolderId).delete
    }
  }
}

