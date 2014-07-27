package actors

import akka.actor.{Props, Actor}
import org.apache.lucene.document._
import utils._
import lucene._
import services._


class IndexSearcherActor extends Actor {
  
  def receive = {
      case MessageSearch(userId, searchText) => {
              try {
                val searcher = getSearcher
                val documents = searcher.searchInDocuments(userId, searchText)
                searcher.close
                sender ! MessageSearchResult(userId, documents)
              } catch {
                case e: Exception =>
                  sender ! akka.actor.Status.Failure(e)
                  throw e
              }
            }

      case MessageSearchWithHighlighter(userId, documentId, searchText) => {
              try {
                val searcher = getSearcher
                val results = searcher.searchInDocumentWithHighlighter(userId, documentId, searchText)
                searcher.close
                sender ! MessageSearchResultWithHighlighter(userId, documentId, results)
              } catch {
                case e: Exception =>
                  sender ! akka.actor.Status.Failure(e)
                  throw e
              }
            }
      case _ => 
    }
  
  private def getSearcher() = {
    new LuceneSearcher(Configuration.luceneIndexPath) 
  }
}

