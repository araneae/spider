package lucene

import org.apache.lucene.document._
import org.junit.Test

class LuceneSearcherTest {

  @Test def testFindDocuments() {
      val searcher = new LuceneSearcher("/tmp/lucene")
      searcher.searchInDocuments(10, "arjun")
      searcher.close
  }
}
