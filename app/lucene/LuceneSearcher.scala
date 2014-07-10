package lucene;

import java.io.File
import scala.annotation.meta.field
import scala.collection.mutable.ListBuffer
import org.apache.lucene.analysis.core.SimpleAnalyzer
import org.apache.lucene.document._
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util._
import org.apache.lucene.util._
import enums._
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.queryparser.complexPhrase.ComplexPhraseQueryParser

class LuceneSearcher(indexDir: String) {

  val indexDirFile = new File(indexDir)
  val dir = FSDirectory.open(indexDirFile)
  val indexReader = DirectoryReader.open(dir)
  var indexSearcher : Option[IndexSearcher] = Some(new IndexSearcher(indexReader))

  def searchResume(userId: Long, searchText: String) : Option[List[models.dtos.Document]] = {
    println(s"in searchResume($userId, $searchText)")
    indexSearcher match {
        case Some(searcher) =>
            val analyzer = new StandardAnalyzer(Version.LUCENE_47)
            val parser = new ComplexPhraseQueryParser(Version.LUCENE_47, "resume", analyzer)
            val searchTextUpper = searchText.toUpperCase
            val query = parser.parse(searchTextUpper)
            
            println(s"query : ${query}")
      
            val numResults = 100
            val hits = searcher.search(query, numResults).scoreDocs
            var list = new ListBuffer[models.dtos.Document]()
            hits.map{ doc => 
                      val document = searcher.doc(doc.doc)
                        list += models.dtos.Document(Some(document.get("id").toLong),
                                          document.get("userId").toLong,
                                          document.get("name"),
                                          DocumentType(document.get("documentType").toInt),
                                          FileType(document.get("fileType").toInt),
                                          document.get("fileName"),
                                          document.get("physicalName"),
                                          document.get("description")
                                          )
                    }
            Some(list.toList)

        case None => None
    }
  }
  
  def close = {
      indexSearcher match {
          case Some(searcher) => indexReader.close()
                                 indexSearcher = None
          case None =>
      }
  }
}
