package actors

import akka.actor.{Props, Actor}
import org.apache.lucene.document._
import utils._
import lucene._
import services._


class IndexWriterActor extends Actor {
  
  def receive = {
      case msgDoc: MessageDocument => {
            val doc = msgDoc.document
            doc.id match {
              case Some(id) => {
                      val filePath =  Configuration.uploadFilePath(msgDoc.userId, doc.physicalName)
                      val textData = FileParserService.parse(doc.fileType, filePath)
                      textData match {
                            case Some(resume) =>  val writer = getWriter
                                                  writer.addOrUpdateDocument(doc.userId, doc, resume)
                                                  writer.close
                            case None =>
                          }
                    }
              case None =>
            }
          }
      case _ => 
    }
  
  private def getWriter() : LuceneWriter = {
    val writer = new LuceneWriter(Configuration.luceneIndexPath)
    writer.create
    writer
  }
}