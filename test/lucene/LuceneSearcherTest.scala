package lucene

import org.apache.lucene.document._
import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import _root_.utils.FileUtil
import lucene.helper._
import org.junit.Assert.fail
import models.dtos._

class LuceneSearcherTest extends LuceneConsts {

  val indexPath = "/tmp/lucene"

  @Before
  def createIndex() {
    FileUtil.deleteDirectory(indexPath)
    val writer = new LuceneWriter(indexPath)
    writer.create

    // add first text document 
    val docId = 10
    val text = "Java Oracle Mobile Hadoop"
    val doc = LuceneDocumentService.getTextDocument(docId, text)
    writer.addOrUpdateDocument(DOC_TYPE_TEXT, docId, doc)
    
    // add second text document
    val docId2 = 20
    val text2 = "Software Engineer"
    val doc2 = LuceneDocumentService.getTextDocument(docId2, text2)
    writer.addOrUpdateDocument(DOC_TYPE_TEXT, docId2, doc2)
    
    // add first user document
    val userId = 10
    val user = User(Some(userId), "John", "Brown", "john@abc.com", "abc")
    val userDoc = LuceneDocumentService.getUserDocument(docId, user)
    writer.addOrUpdateDocument(DOC_TYPE_USER, userId, userDoc)
    
    // add second user document
    val userId2 = 20
    val user2 = User(Some(userId), "Jenny", "Brown", "jenny@abc.com", "abc")
    val userDoc2 = LuceneDocumentService.getUserDocument(docId2, user2)
    writer.addOrUpdateDocument(DOC_TYPE_USER, userId2, userDoc2)
    
    writer.close
  }
  
  @Test
  def testGetDocumentsMultipleTermsFromSingleDocument() {
      val searcher = new LuceneSearcher(indexPath)
      val query = DocumentQueryGenerator.getQuery("java and mobile")
      val filter = DocumentQueryGenerator.getFilter(List(10, 20))
      val optDocuments = searcher.getDocuments(query, filter)
      optDocuments match {
        case Some(documents) =>
             assertEquals("Unable to find correct number of documents", 1, documents.length)
             val doc = documents(0)
             assertEquals("Unable to find the document", "10", doc.get(FIELD_DOCUMENT_ID))
             assertEquals("Unable to find the document", s"${DOC_TYPE_TEXT}-10", doc.get(FIELD_DOC_ID))
        case _ => fail("Unable to find document")
      }
      
      searcher.close
  }
  
  @Test
  def testGetDocumentsMultipleTermsFromMultipleDocuments() {
      val searcher = new LuceneSearcher(indexPath)
      val query = DocumentQueryGenerator.getQuery("Software and Engineer")
      val filter = DocumentQueryGenerator.getFilter(List(10, 20))
      val optDocuments = searcher.getDocuments(query, filter)
      optDocuments match {
        case Some(documents) =>
             assertEquals("Unable to find correct number of documents", 1, documents.length)
             val doc = documents(0)
             assertEquals("Unable to find the document", "20", doc.get(FIELD_DOCUMENT_ID))
             assertEquals("Unable to find the document", s"${DOC_TYPE_TEXT}-20", doc.get(FIELD_DOC_ID))
        case _ => fail("Unable to find document")
      }
      
      searcher.close
  }
  
  @Test
  def testGetTextDocumentsWithSingleTerm() {
      val searcher = new LuceneSearcher(indexPath)
      val query = DocumentQueryGenerator.getQuery("Java")
      val filter = DocumentQueryGenerator.getFilter(List(10, 20))
      val optDocuments = searcher.getDocuments(query, filter)
      optDocuments match {
        case Some(documents) =>
             assertEquals("Unable to find correct number of documents", 1, documents.length)
             val doc = documents(0)
             assertEquals("Unable to find the document", "10", doc.get(FIELD_DOCUMENT_ID))
             assertEquals("Unable to find the document", "text-10", doc.get(FIELD_DOC_ID))
        case _ => fail("Unable to find document")
      }
      
      searcher.close
  }
  
  @Test
  def testGetTextDocumentsWithOrOperator() {
      val searcher = new LuceneSearcher(indexPath)
      val query = DocumentQueryGenerator.getQuery("Java or Engineer")
      val filter = DocumentQueryGenerator.getFilter(List(10, 20))
      val optDocuments = searcher.getDocuments(query, filter)
      optDocuments match {
        case Some(documents) =>
             assertEquals("Unable to find correct number of documents", 2, documents.length)
             val doc = documents(0)
             val doc2 = documents(1)
             val expectedDocumentIds = List("10", "20")
             val expectedDocIds = List("text-10", "text-20")
             assertTrue("Unable to find the document", expectedDocumentIds.contains(doc.get(FIELD_DOCUMENT_ID)))
             assertTrue("Unable to find the document", expectedDocIds.contains(doc.get(FIELD_DOC_ID)))
             assertTrue("Unable to find the document", expectedDocumentIds.contains(doc2.get(FIELD_DOCUMENT_ID)))
             assertTrue("Unable to find the document", expectedDocIds.contains(doc2.get(FIELD_DOC_ID)))
        case _ => fail("Unable to find document")
      }
      
      searcher.close
  }
 
  @Test
  def testGetUserDocumentsWithFirstName() {
      val searcher = new LuceneSearcher(indexPath)
      val query = UserQueryGenerator.getQuery("John")
      val filter = UserQueryGenerator.getFilter(List())
      val optDocuments = searcher.getDocuments(query, filter)
      optDocuments match {
        case Some(documents) =>
             assertEquals("Unable to find correct number of user documents", 1, documents.length)
             val doc = documents(0)
             assertEquals("Unable to find the user document", "10", doc.get(FIELD_USER_ID))
             assertEquals("Unable to find the user document", s"${DOC_TYPE_USER}-10", doc.get(FIELD_DOC_ID))
        case _ => fail("Unable to find user document")
      }
      
      searcher.close
  }
  
  @Test
  def testGetUserDocumentsWithLastName() {
      val searcher = new LuceneSearcher(indexPath)
      val query = UserQueryGenerator.getQuery("brown")
      val filter = UserQueryGenerator.getFilter(List())
      val optDocuments = searcher.getDocuments(query, filter)
      optDocuments match {
        case Some(documents) =>
             assertEquals("Unable to find correct number of user documents", 2, documents.length)
             val doc = documents(0)
             val doc2 = documents(1)
             val expectedUserIds = List("10", "20")
             val expectedDocIds = List("user-10", "user-20")
             assertTrue("Unable to find the document", expectedUserIds.contains(doc.get(FIELD_USER_ID)))
             assertTrue("Unable to find the document", expectedDocIds.contains(doc.get(FIELD_DOC_ID)))
             assertTrue("Unable to find the document", expectedUserIds.contains(doc2.get(FIELD_USER_ID)))
             assertTrue("Unable to find the document", expectedDocIds.contains(doc2.get(FIELD_DOC_ID)))
        case _ => fail("Unable to find user document")
      }
      
      searcher.close
  }
  
  @Test
  def testGetUserDocumentsWithLastNameUpperCase() {
      val searcher = new LuceneSearcher(indexPath)
      val query = UserQueryGenerator.getQuery("BROWN")
      val filter = UserQueryGenerator.getFilter(List())
      val optDocuments = searcher.getDocuments(query, filter)
      optDocuments match {
        case Some(documents) =>
             assertEquals("Unable to find correct number of user documents", 2, documents.length)
             val doc = documents(0)
             val doc2 = documents(1)
             val expectedUserIds = List("10", "20")
             val expectedDocIds = List("user-10", "user-20")
             assertTrue("Unable to find the document", expectedUserIds.contains(doc.get(FIELD_USER_ID)))
             assertTrue("Unable to find the document", expectedDocIds.contains(doc.get(FIELD_DOC_ID)))
             assertTrue("Unable to find the document", expectedUserIds.contains(doc2.get(FIELD_USER_ID)))
             assertTrue("Unable to find the document", expectedDocIds.contains(doc2.get(FIELD_DOC_ID)))
        case _ => fail("Unable to find user document")
      }
      
      searcher.close
  }
  
  @Test
  def testGetUserDocumentsWithEmail() {
      val searcher = new LuceneSearcher(indexPath)
      val query = UserQueryGenerator.getQuery("john@abc.com")
      val filter = UserQueryGenerator.getFilter(List())
      val optDocuments = searcher.getDocuments(query, filter)
      optDocuments match {
        case Some(documents) =>
             assertEquals("Unable to find correct number of user documents", 1, documents.length)
             val doc = documents(0)
             assertEquals("Unable to find the document", "10", doc.get(FIELD_USER_ID))
             assertEquals("Unable to find the document", "user-10", doc.get(FIELD_DOC_ID))
        case _ => fail("Unable to find user document")
      }
      
      searcher.close
  }
  
  @Test
  def testGetDocumentsEmptyResults() {
      val searcher = new LuceneSearcher(indexPath)
      val query = DocumentQueryGenerator.getQuery("java and mobile")
      val filter = DocumentQueryGenerator.getFilter(List(20))
      val optDocuments = searcher.getDocuments(query, filter)
      optDocuments match {
        case Some(documents) =>
             assertEquals("Found unexpected documents", 0, documents.length)
        case _ => 
      }
      
      searcher.close
  }
    
  @After
  def cleanUp() {
    FileUtil.deleteDirectory(indexPath)
  }
  
}
