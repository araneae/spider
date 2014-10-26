package lucene;

import java.io.File
import org.apache.lucene.util._
import org.apache.lucene.index._
import org.apache.lucene.store._
import org.apache.lucene.document._
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.search._
import services._

class LuceneWriter(indexDir: String) extends LuceneConsts {
  
  var writer : Option[IndexWriter] = None 
  val analyzer = new StandardAnalyzer(Version.LUCENE_47)

  def create() = {
    writer match {
      case None =>
              var create = true;
              val indexDirFile = new File(indexDir)
              if (indexDirFile.exists() && indexDirFile.isDirectory()) {
                  create = false
              }
              
              val dir = new NIOFSDirectory(indexDirFile)
              val iwc = new IndexWriterConfig(Version.LUCENE_47, analyzer)
              iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND)
              val wr = new IndexWriter(dir, iwc)
              if (!create) wr.commit
              writer = Some(wr)

      case Some(writer) =>
    }
  }
  
  def close() = {
    writer match {
      case Some(wr) => analyzer.close()
                       wr.close(true)
                       writer = None
      case None => 
    }
  }
  
  def addOrUpdateDocument(docType: String, docId: Long, document: Document) = {
    writer match {
      case Some(wr) => {
                  wr.updateDocument(getDocIdTerm(docType, docId), document)
                  wr.commit()
               }
      case None => 
    }
  }
  
  def deleteDocument(docType: String, docId: Long) = {
    writer match {
      case Some(wr) => {
                  wr.deleteDocuments(getDocIdTerm(docType, docId))
                  wr.commit()
               }
      case None => 
    }
  }
  
}
