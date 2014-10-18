package lucene.helper

import org.apache.lucene.queryparser.complexPhrase.ComplexPhraseQueryParser
import org.apache.lucene.search.BooleanQuery
import scala.collection.mutable.ListBuffer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.search._
import org.apache.lucene.util._
import org.apache.lucene.index.Term
import lucene._

object DocumentQueryGenerator extends QueryGenerator with LuceneConsts {
  
  val analyzer = new StandardAnalyzer(Version.LUCENE_47)
  
  override def getQuery(searchText: String) : Query = {
    val parser = new ComplexPhraseQueryParser(Version.LUCENE_47, FIELD_CONTENTS, analyzer)
    val searchTextUpper = searchText.toUpperCase
    val query = parser.parse(searchTextUpper)
    query
  }
  
  override def getFilter(ids : Seq[Long]) : Filter = {
    val booleanQuery = new BooleanQuery();
    if (ids.length > 0) booleanQuery.setMinimumNumberShouldMatch(1)
    ids.map { docId =>
        val term = new TermQuery(new Term(FIELD_DOCUMENT_ID, docId.toString))
        booleanQuery.add(term, BooleanClause.Occur.SHOULD);
    }
    
    val term = new TermQuery(new Term(FIELD_DOCUMENT_TYPE, DOC_TYPE_TEXT))
    booleanQuery.add(term, BooleanClause.Occur.MUST);
    
    val filter = new QueryWrapperFilter(booleanQuery)
    filter
  }

}