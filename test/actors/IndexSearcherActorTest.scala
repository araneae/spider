package actors

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.testkit.{ TestActors, TestKit, ImplicitSender }
import org.scalatest.WordSpecLike
import akka.testkit.DefaultTimeout
import org.scalatest.Matchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.joda.time.DateTime
import _root_.utils.FileUtil
import lucene._
import lucene.helper._
import models.dtos._
import utils._
import play.api.Play
import play.api.test.FakeApplication
import enums._
 
@RunWith(classOf[JUnitRunner])
class IndexSearcherActorTest(_system: ActorSystem) extends TestKit(_system)
  with DefaultTimeout
  with ImplicitSender
  with WordSpecLike 
  with Matchers 
  with BeforeAndAfterAll 
  with LuceneConsts {
  
  val indexPath = "/private/tmp/lucene"
 
  def this() = this(ActorSystem("MySpec"))
 
  override def afterAll {
    TestKit.shutdownActorSystem(system)
    FileUtil.deleteDirectory(indexPath)
  }
  
  override def beforeAll {
    createIndex
  }
 
  "Lucene Search Actor" must {
 
    "send back search result" in {
      //val echo = system.actorOf(TestActors.echoActorProps)
      val indexSearcherActor = system.actorOf(Props[IndexSearcherActor])
      indexSearcherActor ! MessageDocumentSearch(List(10), "java")
      expectMsg(MessageDocumentSearchResult(List(10)))
    }
 
  }
  
  def createIndex() {
    FileUtil.deleteDirectory(indexPath)
    val writer = new LuceneWriter(indexPath)
    writer.create

    // add first text document 
    val docId = 10
    val document = Document(Some(docId), 1, "Test Document", DocumentType.TEXT,
                   FileType.DOC, "Test Document", "6118268", "This is a test document", "signature", 1)
    val text = "Java Oracle Mobile Hadoop"
    val doc = LuceneDocumentService.getTextDocument(document, text)
    writer.addOrUpdateDocument(DOC_TYPE_TEXT, docId, doc)
    
    // add second text document
    val docId2 = 20
    val document2 = Document(Some(docId2), 1, "Test Document 2", DocumentType.TEXT,
                   FileType.DOC, "Test Document 2", "6118268", "This is a test document 2", "signature", 1)
    val text2 = "Software Engineer"
    val doc2 = LuceneDocumentService.getTextDocument(document2, text2)
    writer.addOrUpdateDocument(DOC_TYPE_TEXT, docId2, doc2)
    
    // add first user document
    val userId = 10
    val countryId = 1
    val user = User(Some(userId), "John", None, "Brown", "john@abc.com", None, "abc", countryId, "token",
                            false, new DateTime, UserStatusType.ACTIVE, None)
    val userDoc = LuceneDocumentService.getUserDocument(docId, user)
    writer.addOrUpdateDocument(DOC_TYPE_USER, userId, userDoc)
    
    // add second user document
    val userId2 = 20
    val user2 = User(Some(userId), "Jenny", None, "Brown", "jenny@abc.com", None, "abc", countryId, "token",
                            false, new DateTime, UserStatusType.ACTIVE, None)
    val userDoc2 = LuceneDocumentService.getUserDocument(docId2, user2)
    writer.addOrUpdateDocument(DOC_TYPE_USER, userId2, userDoc2)
    
    writer.close
  }
}