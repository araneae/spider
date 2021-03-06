package actors

import akka.actor.{Props, Actor}
import scala.collection.mutable.ListBuffer
import org.apache.lucene.document._
import utils._
import lucene._
import services._
import lucene.helper._

class IndexSearcherActor extends Actor with LuceneConsts {
  
  def receive = {
      case MessageDocumentSearch(documentFolderIds, searchText) => {
          try {
            val searcher = getSearcher
            val query = DocumentQueryGenerator.getQuery(searchText)
            val filter = DocumentQueryGenerator.getFilter(FIELD_DOCUMENT_FOLDER_ID, documentFolderIds)
            // search in lucene index
            val optDocuments = searcher.getDocuments(query, filter)
            // get the return message
            val message = DocumentQueryResultProcessor.getMessage(optDocuments)
            
            searcher.close
            sender ! message
          } catch {
            case e: Exception =>
              sender ! akka.actor.Status.Failure(e)
              throw e
          }
      }

      case MessageSearchWithHighlighter(documentId, searchText) => {
          try {
            val searcher = getSearcher
            val query = DocumentQueryGenerator.getQuery(searchText)
            val filter = DocumentQueryGenerator.getFilter(List(documentId))
            // search in lucene index
            val optDocResults = searcher.getHighlights(query, filter)
            searcher.close
            sender ! MessageSearchResultWithHighlighter(documentId, optDocResults.getOrElse(List.empty))
          } catch {
            case e: Exception =>
              sender ! akka.actor.Status.Failure(e)
              throw e
          }
      }
      
      case MessageUserSearch(searchText) => {
          try {
            val searcher = getSearcher
            val query = UserQueryGenerator.getQuery(searchText)
            val filter = UserQueryGenerator.getFilter(List())
            // search in lucene index
            val optDocuments = searcher.getDocuments(query, filter)
            var userIds = new ListBuffer[Long]()
            optDocuments match {
              case Some(list) =>
                      list.map { doc =>
                          val userId = doc.get(FIELD_USER_ID)
                          userIds += userId.toLong
                      }
              case _ =>  
            }
            
            searcher.close
            sender ! MessageUserSearchResult(userIds.toList)
          } catch {
            case e: Exception =>
              sender ! akka.actor.Status.Failure(e)
              throw e
          }
      }
      
      case MessageDocumentGetContents(documentId) => {
          try {
            val searcher = getSearcher
            // search in lucene index
            val optDocuments = searcher.getDocument(DOC_TYPE_TEXT, documentId)
            val message = optDocuments match {
              case Some(document) =>
                  val contents = document.get(FIELD_CONTENTS)
                  MessageDocumentContents(documentId, HtmlUtil.sanitize(contents))
              case _ =>
                  MessageDocumentContents(documentId, TEXT_EMPTY)
            }
            
            searcher.close
            sender ! message
          } catch {
            case e: Exception =>
              sender ! akka.actor.Status.Failure(e)
              throw e
          }
      }
      
      case MessageJobSearch(jobSearchDTO) => {
          try {
            val searcher = getSearcher
            val query = JobQueryGenerator.getQuery(jobSearchDTO)
            val filter = JobQueryGenerator.getFilter()
            // search in lucene index
            val optDocuments = searcher.getDocuments(query, filter)
            // get the return message
            val message = JobQueryResultProcessor.getMessage(optDocuments)
            
            searcher.close
            sender ! message
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

