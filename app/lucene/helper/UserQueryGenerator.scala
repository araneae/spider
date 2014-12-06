package lucene.helper

import org.apache.lucene.queryparser.complexPhrase.ComplexPhraseQueryParser
import org.apache.lucene.search.BooleanQuery
import scala.collection.mutable.ListBuffer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.search._
import org.apache.lucene.util._
import org.apache.lucene.index.Term
import lucene._

object UserQueryGenerator extends QueryGenerator with LuceneConsts {
  
  val analyzer = new StandardAnalyzer(Version.LUCENE_47)
  
  override def getQuery(searchText: String) : Query = {
    val parser = new ComplexPhraseQueryParser(Version.LUCENE_47, FIELD_CONTENTS, analyzer)
    val searchTextUpper = searchText.replaceAll("@", " AND ").toUpperCase
    val query = parser.parse(s"${searchTextUpper}")
    query
  }
  
  override def getFilter(fieldName: String, ids : Seq[Long]) : Filter = {
    val booleanQuery = new BooleanQuery();
    if (ids.length > 0) booleanQuery.setMinimumNumberShouldMatch(1)
    ids.map { userId =>
        booleanQuery.add(new TermQuery(new Term(fieldName, userId.toString)), BooleanClause.Occur.SHOULD);
    }
    
    val term = new TermQuery(new Term(FIELD_DOCUMENT_TYPE, DOC_TYPE_USER))
    booleanQuery.add(term, BooleanClause.Occur.MUST);
    
    val filter = new QueryWrapperFilter(booleanQuery)
    filter
  }
  
  override def getFilter(ids : Seq[Long]) : Filter = {
    getFilter(FIELD_USER_ID, ids)
  }

}