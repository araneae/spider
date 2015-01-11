package lucene

import org.apache.lucene.document.FieldType
import org.apache.lucene.search.vectorhighlight.SimpleFragListBuilder
import org.apache.lucene.search.vectorhighlight.FastVectorHighlighter
import org.apache.lucene.search.vectorhighlight.ScoreOrderFragmentsBuilder
import org.apache.lucene.search.vectorhighlight.BaseFragmentsBuilder
import org.apache.lucene.util._
import org.apache.lucene.index._
import org.apache.lucene.store._
import org.apache.lucene.document._

trait LuceneConsts {
  
	val TEXT_EMPTY = ""
  // common fields
  val FIELD_DOC_ID = "docId"
  
  // document related fields
  val DOC_TYPE_TEXT = "text"
  val FIELD_DOCUMENT_ID = "documentId"
  val FIELD_DOCUMENT_FOLDER_ID = "documentFolderId"
  val FIELD_DOCUMENT_TYPE = "documentType"
  val FIELD_CONTENTS = "contents"
  
  // user related fields
  val DOC_TYPE_USER = "user"
  val FIELD_USER_ID = "userId"
  val FIELD_FIRST_NAME = "firstName"
  val FIELD_LAST_NAME = "lastName"
  val FIELD_EMAIL = "email"
  
  // job related fields
  val DOC_TYPE_JOB = "job"
  val FIELD_JOB_ID = "jobRequirementId"
  val FIELD_JOB_TITLE = "title"
  val FIELD_JOB_COMPANY_ID = "companyId"
  val FIELD_JOB_COMPANY_NAME = "companyName"
  val FIELD_JOB_STATUS = "status"
  val FIELD_JOB_LOCATION = "location"
  val FIELD_JOB_LOCATION_LAT = "locationLat"
  val FIELD_JOB_LOCATION_LNG = "locationLng"
  val FIELD_JOB_POST_DATE = "postDate"
  val FIELD_JOB_DESCRIPTION = "description"
    
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
  
  def getDocIdTerm(docType: String, docId: Long) = new Term(FIELD_DOC_ID, s"${docType}-${docId}")
}