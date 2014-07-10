package actors

import akka.actor.{Props, Actor}
import org.apache.lucene.document._
import utils._
import lucene._
import services._


class IndexSearcherActor extends Actor {
  
  def receive = {
      case msgSearch: MessageSearch => {
              try {
                val userId = msgSearch.userId
                val searchText = msgSearch.searchText
                val searcher = getSearcher
                val list = searcher.searchResume(userId, searchText)
                searcher.close
                sender ! MessageSearchResult(userId, list)
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

