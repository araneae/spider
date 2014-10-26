package lucene;

import java.io.File
import scala.annotation.meta.field
import scala.collection.mutable.ListBuffer
import org.apache.lucene.analysis.core.SimpleAnalyzer
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.queryparser.complexPhrase.ComplexPhraseQueryParser
import org.apache.lucene.search.vectorhighlight._
import org.apache.lucene.document.Document
import org.apache.lucene.search._
import org.apache.lucene.index._
import org.apache.lucene.util._
import org.joda.time.DateTime
import models.dtos._
import enums._
import utils._
import models.dtos._
import _root_.utils.HtmlUtil

class LuceneSearcher(indexDir: String) extends LuceneConsts {

  val indexDirFile = new File(indexDir)
  val dir = FSDirectory.open(indexDirFile)
  val indexReader = DirectoryReader.open(dir)
  var indexSearcher : Option[IndexSearcher] = Some(new IndexSearcher(indexReader))
  
  def getScoreDocs(query: Query, filter: Filter) : Option[Array[ScoreDoc]] = {
    indexSearcher match {
        case Some(searcher) =>
            val hits = searcher.search(query, filter, NUM_QUERY_RESULTS).scoreDocs
            Some(hits)
        case None => None
    }
  }
  
  def getDocuments(query: Query, filter: Filter) : Option[Seq[Document]] = {
    println(s"LuceneSearcher.getDocuments(${query}, ${filter})")
    indexSearcher match {
        case Some(searcher) =>
          val optScoreDocs = getScoreDocs(query, filter)
           optScoreDocs match {
             case Some(scoreDocs) =>
               var list = new ListBuffer[Document]()
                scoreDocs.map { scoreDoc => 
                    val document = searcher.doc(scoreDoc.doc)
                    list += document
                }
               //println(s"result ${list}")
               Some(list)
             case None => None
           }
        case None => None
    }
  }
  
  def getHighlights(query: Query, filter: Filter) : Option[List[DocumentSearchResult]] = {
    println(s"LuceneSearcher.getHighlights(${query}, ${filter})")
    indexSearcher match {
        case Some(searcher) =>
            val optScoreDocs = getScoreDocs(query, filter)
            optScoreDocs match {
               case Some(scoreDocs) =>
                    val fieldQuery = fastVectorHighlighter.getFieldQuery(query);
                    var list = new ListBuffer[DocumentSearchResult]()
                    scoreDocs.map { scoreDoc =>
                      val document = searcher.doc(scoreDoc.doc)
                      val snippets = fastVectorHighlighter.getBestFragments(
                                          fieldQuery, searcher.getIndexReader(),
                                          scoreDoc.doc, FIELD_CONTENTS, FRAG_CHARS_SIZE, MAX_NUM_FRAGEMENTS)
                      if (snippets != null) {
                        val id = document.get(FIELD_DOCUMENT_ID)
                         list += DocumentSearchResult(id.toLong, HtmlUtil.sanitize(snippets))
                      }
                    }
                    Some(list.toList)

               case None => None
           }
           
        case None => None
    }
  }
  
  def getDocument(docType: String, docId: Long) : Option[Document] = {
    println(s"LuceneSearcher.getDocument(${docType}, ${docId})")
    indexSearcher match {
        case Some(searcher) =>
          val query = new TermQuery(getDocIdTerm(docType, docId));
          val scoreDocs = searcher.search(query, 1).scoreDocs
          scoreDocs.map { scoreDoc => 
            val document = searcher.doc(scoreDoc.doc)
            return Some(document)
          }
          None
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
