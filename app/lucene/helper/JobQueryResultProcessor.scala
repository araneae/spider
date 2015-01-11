package lucene.helper

import org.apache.lucene.search.Query
import org.apache.lucene.search.Filter
import org.apache.lucene.document.Document
import scala.collection.mutable.ListBuffer
import lucene.LuceneConsts
import actors._

object JobQueryResultProcessor extends LuceneConsts {

  def getMessage(optDocuments: Option[Seq[Document]]) : Message = {
      var list = new ListBuffer[Long]()
      optDocuments match {
        case Some (documents) =>
          documents.map { doc =>
            list += doc.get(FIELD_JOB_ID).toLong
          }
        case None => 
      }
      MessageJobSearchResult(list.toList)
  }
  
}