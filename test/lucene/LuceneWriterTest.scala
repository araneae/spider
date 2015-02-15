package lucene

import org.junit.Test
import org.junit.Assert.assertTrue
import parsers.TxtFileParser
import lucene.helper.LuceneDocumentService
import _root_.utils.FileUtil
import org.junit.Before
import org.junit.After
import models.dtos._
import enums._

class LuceneWriterTest extends LuceneConsts {
  
  val indexPath = "/tmp/lucene"
  
  @Before
  def createIndex() {
    FileUtil.deleteDirectory(indexPath)
    val writer = new LuceneWriter(indexPath)
    writer.create
    writer.close
  }
  
  @Test 
  def testAddOrUpdateDocument() {
      val writer = new LuceneWriter(indexPath)
      val reader = new TxtFileParser()
      val text = reader.parse("test/lucene/SampleResume.txt")
      val docId = 10
      val document = Document(Some(docId), 1, "Test Document", DocumentType.TEXT,
                   FileType.DOC, "Test Document", "6118268", "This is a test document", "signature", 1)
      val doc = LuceneDocumentService.getTextDocument(document, text)
      writer.addOrUpdateDocument(DOC_TYPE_TEXT, docId, doc)
      writer.close()
      
      assertTrue("Unable to create index", FileUtil.isDirExists(indexPath))
  }
  
  @After
  def cleanUp() {
    FileUtil.deleteDirectory(indexPath)
  }
}
