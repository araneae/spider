package actors

import akka.actor.{Props, Actor}
import org.apache.lucene.document._
import utils._
import lucene._
import services._
import lucene.helper._

class IndexWriterActor extends Actor with LuceneConsts {
  
  def receive = {
      case MessageAddDocument(document) => {
            val filePath =  Configuration.uploadFilePath(document.physicalName)
            val textData = FileParserService.parse(document.fileType, filePath)
            textData match {
                  case Some(contents) =>  val writer = getWriter
                            val luceneDocument = LuceneDocumentService.getTextDocument(document, contents)
                            writer.addOrUpdateDocument(DOC_TYPE_TEXT, document.documentId.get, luceneDocument)
                            writer.close
                  case None =>
                }
          }
      
      case MessageDeleteDocument(docId) => {
                      val writer = getWriter
                      writer.deleteDocument(DOC_TYPE_TEXT, docId)
                      writer.close
          }
      
      case MessageAddUser(user) => {
            user.userId match {
              case Some(userId) =>
                      val writer = getWriter
                      val document = LuceneDocumentService.getUserDocument(userId, user)
                      writer.addOrUpdateDocument(DOC_TYPE_USER, userId, document)
                      writer.close
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