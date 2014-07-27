package lucene;

import org.apache.lucene.analysis.standard._
import org.apache.lucene.util._
import org.apache.lucene.store.NIOFSDirectory
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.store.Directory
import org.apache.lucene.analysis.Analyzer
import java.io.File
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.document._
import org.apache.lucene.analysis.core.StopAnalyzer
import org.apache.lucene.index.Term

class LuceneWriter(indexDir: String) {
  
  var writer : Option[IndexWriter] = None 
  val analyzer = new StopAnalyzer(Version.LUCENE_47)
  val highlighterType = new FieldType()
  highlighterType.setIndexed(true)
  highlighterType.setOmitNorms(false)
  highlighterType.setStored(true)
  highlighterType.setStoreTermVectors(true)
  highlighterType.setStoreTermVectorPositions(true)
  highlighterType.setStoreTermVectorOffsets(true)

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
  
  def addOrUpdateDocument(userId: Long, doc: models.dtos.Document, resume: String) = {
    writer match {
      case Some(wr) => {
                doc.id match {
                  case Some(id) =>
                      var luceneDocument = new Document()
                      luceneDocument.add(new StringField("id", id.toString, Field.Store.YES))
                      luceneDocument.add(new StringField("userId", doc.userId.toString, Field.Store.YES))
                      luceneDocument.add(new StringField("name", doc.name, Field.Store.YES))
                      luceneDocument.add(new StringField("documentType", doc.documentType.id.toString, Field.Store.YES))
                      luceneDocument.add(new StringField("fileType", doc.fileType.id.toString, Field.Store.YES))
                      luceneDocument.add(new StringField("fileName", doc.fileName, Field.Store.YES))
                      luceneDocument.add(new StringField("physicalName", doc.physicalName, Field.Store.YES))
                      luceneDocument.add(new StringField("description", doc.description, Field.Store.YES))
                      luceneDocument.add(new Field("resume", resume, highlighterType))

                      wr.updateDocument(new Term("id", id.toString), luceneDocument)
                      wr.commit()
                  case None =>
                  }
               }
      case None => 
    }
  }
  
  def deleteDocument(userId: Long, id: Long) = {
    writer match {
      case Some(wr) => {
                  wr.deleteDocuments(new Term("id", id.toString))
                  wr.commit()
               }
      case None => 
    }
  }
  
}
