package lucene.helper

import org.apache.lucene.search.Query
import org.apache.lucene.search.Filter

abstract class QueryGenerator {

  def getQuery(searchText: String) : Query
  
  def getFilter(ids: Seq[Long]) : Filter
  
}