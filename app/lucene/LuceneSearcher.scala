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
import org.apache.lucene.document._
import org.apache.lucene.search._
import org.apache.lucene.index._
import org.apache.lucene.util._
import models.dtos._
import enums._
import utils._

class LuceneSearcher(indexDir: String) {

  val indexDirFile = new File(indexDir)
  val dir = FSDirectory.open(indexDirFile)
  val indexReader = DirectoryReader.open(dir)
  var indexSearcher : Option[IndexSearcher] = Some(new IndexSearcher(indexReader))
  
  // highlighter
  val NUM_QUERY_RESULTS = 100
  val FRAG_CHARS_SIZE = 200
  val MAX_NUM_FRAGEMENTS = 100
  val COLORED_PRE_TAGS = Array(
        "<b class=\"mark-yellow\">", "<b class=\"mark-lawngreen\">", "<b class=\"mark-aquamarine\">",
        "<b class=\"mark-magenta\">", "<b class=\"mark-palegreen\">", "<b class=\"mark-coral\">",
        "<b class=\"mark-wheat\">", "<b class=\"mark-khaki\">", "<b class=\"mark-lime\">",
        "<b class=\"mark-deepskyblue\">", "<b class=\"mark-deeppink\">", "<b class=\"mark-salmon\">",
        "<b class=\"mark-peachpuff\">", "<b class=\"mark-violet\">", "<b class=\"mark-mediumpurple\">",
        "<b class=\"mark-palegoldenrod\">", "<b class=\"mark-darkkhaki\">", "<b class=\"mark-springgreen\">",
        "<b class=\"mark-turquoise\">", "<b class=\"mark-powderblue\">"
  )
  val fragListBuilder = new SimpleFragListBuilder()
  val fragmentBuilder = new ScoreOrderFragmentsBuilder(COLORED_PRE_TAGS,
                                                       BaseFragmentsBuilder.COLORED_POST_TAGS)
  val fastVectorHighlighter = new FastVectorHighlighter(true, true, fragListBuilder, fragmentBuilder)

  def searchInDocuments(userId: Long, searchText: String) : Option[List[models.dtos.Document]] = {
    println(s"in searchResume($userId, $searchText)")
    indexSearcher match {
        case Some(searcher) =>
            val analyzer = new StandardAnalyzer(Version.LUCENE_47)
            val parser = new ComplexPhraseQueryParser(Version.LUCENE_47, "resume", analyzer)
            val searchTextUpper = searchText.toUpperCase
            val query = parser.parse(searchTextUpper)
            
            println(s"query : ${query}")
            // userId filter
            val filter = new QueryWrapperFilter(new TermQuery(new Term("userId", userId.toString)));
      
            val hits = searcher.search(query, filter, NUM_QUERY_RESULTS).scoreDocs
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
  
  def searchInDocumentWithHighlighter(userId: Long, documentId: Long, searchText: String) : Option[List[DocumentSearchResult]] = {
    println(s"in searchHighlighterResume($userId, $documentId, $searchText)")
    indexSearcher match {
        case Some(searcher) =>
            val analyzer = new StandardAnalyzer(Version.LUCENE_47)
            val parser = new ComplexPhraseQueryParser(Version.LUCENE_47, "resume", analyzer)
            val searchTextUpper = searchText.toUpperCase
            val query = parser.parse(searchTextUpper)
            val fieldQuery = fastVectorHighlighter.getFieldQuery(query);
            
            println(s"query : ${query}")
            // userId and document filter
            val booleanQuery = new BooleanQuery();
            booleanQuery.add(new TermQuery(new Term("id", documentId.toString)), BooleanClause.Occur.MUST)
            booleanQuery.add(new TermQuery(new Term("userId", userId.toString)), BooleanClause.Occur.MUST)

            val filter = new QueryWrapperFilter(booleanQuery);

            val hits = searcher.search(query, filter, NUM_QUERY_RESULTS).scoreDocs
            var list = new ListBuffer[DocumentSearchResult]()
            hits.map{ doc => 
                      val snippets = fastVectorHighlighter.getBestFragments(
                                        fieldQuery, searcher.getIndexReader(),
                                        doc.doc, "resume", FRAG_CHARS_SIZE, MAX_NUM_FRAGEMENTS)
                      if (snippets != null) {
                         list += DocumentSearchResult(userId, documentId, HtmleUtil.sanitize(snippets))
                      }
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
