package lucene

import org.apache.lucene.document.FieldType
import org.apache.lucene.search.vectorhighlight.SimpleFragListBuilder
import org.apache.lucene.search.vectorhighlight.FastVectorHighlighter
import org.apache.lucene.search.vectorhighlight.ScoreOrderFragmentsBuilder
import org.apache.lucene.search.vectorhighlight.BaseFragmentsBuilder

trait LuceneConsts {
  
  val FIELD_DOC_ID = "docId"
  val FIELD_DOCUMENT_ID = "documentId"
  val FIELD_USER_ID = "userId"
  val FIELD_DOCUMENT_TYPE = "documentType"
  val FIELD_CONTENTS = "contents"
  val FIELD_FIRST_NAME = "firstName"
  val FIELD_LAST_NAME = "lastName"
  val FIELD_EMAIL = "email"
  val DOC_TYPE_TEXT = "text"
  val DOC_TYPE_USER = "user"
    
  val highlighterType = new FieldType()
  highlighterType.setIndexed(true)
  highlighterType.setOmitNorms(false)
  highlighterType.setStored(true)
  highlighterType.setStoreTermVectors(true)
  highlighterType.setStoreTermVectorPositions(true)
  highlighterType.setStoreTermVectorOffsets(true)
  
  // highlighter
  val NUM_QUERY_RESULTS = 100
  val FRAG_CHARS_SIZE = 200
  val MAX_NUM_FRAGEMENTS = 100
  val COLORED_PRE_TAGS = Array(
        "<b class=\"mark-yellow\">", "<b class=\"mark-lawngreen\">", "<b class=\"mark-aquamarine\">",
        "<b class=\"mark-magenta\">", "<b class=\"mark-palegreen\">", "<b class=\"mark-coral\">",
        "<b class=\"mark-wheat\">", "<b class=\"mark-khaki\">", "<b class=\"mark-lime\">",
        "<b class=\"mark-deepskyblue\">", "<b class=\"mark-deeppink\">", "<b class=\"mark-salmon\">",
        "<b class=\"mark-peachpuff\">", "<b class=\"mark-violet\">", "<b class=\"mark-mediumpurple\">",
        "<b class=\"mark-palegoldenrod\">", "<b class=\"mark-darkkhaki\">", "<b class=\"mark-springgreen\">",
        "<b class=\"mark-turquoise\">", "<b class=\"mark-powderblue\">"
  )
  val fragListBuilder = new SimpleFragListBuilder()
  val fragmentBuilder = new ScoreOrderFragmentsBuilder(COLORED_PRE_TAGS,
                                                       BaseFragmentsBuilder.COLORED_POST_TAGS)
  val fastVectorHighlighter = new FastVectorHighlighter(true, true, fragListBuilder, fragmentBuilder)
}