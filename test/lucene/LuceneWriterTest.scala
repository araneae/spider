package lucene

import org.junit.Test
import parsers._
import models.dtos._
import enums._

class LuceneWriterTest {

  @Test def testAddDocument() {
      val writer = new LuceneWriter("/tmp/lucene")
      val reader = new TxtFileParser()
      val text = reader.parse("test/lucene/SampleResume.txt")
      var doc =Document(Some(1), 10, "Arjun", DocumentType.TEXT,
                FileType.TXT, "SampleResume.txt", "SampleResume.txt", "This is my first resume", 2)
      writer.addOrUpdateDocument(10, doc, text)
      writer.close()
  }
}
