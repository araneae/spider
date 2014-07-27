package actors

import akka.actor.{Props, Actor}
import org.apache.lucene.document._
import utils._
import lucene._
import services._


class IndexWriterActor extends Actor {
  
  def receive = {
      case MessageAddDocument(userId, document) => {
            document.id match {
              case Some(id) => {
                      val filePath =  Configuration.uploadFilePath(document.userId, document.physicalName)
                      val textData = FileParserService.parse(document.fileType, filePath)
                      textData match {
                            case Some(resume) =>  val writer = getWriter
                                                  writer.addOrUpdateDocument(document.userId, document, resume)
                                                  writer.close
                            case None =>
                          }
                    }
              case None =>
            }
          }
      
      case MessageDeleteDocument(userId, id) => {
                      val writer = getWriter
                      writer.deleteDocument(userId, id)
                      writer.close
          }
      
      case _ => 
    }
  
  private def getWriter() : LuceneWriter = {
    val writer = new LuceneWriter(Configuration.luceneIndexPath)
    writer.create
    writer
  }
}