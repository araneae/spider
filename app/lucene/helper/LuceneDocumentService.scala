package lucene.helper

import org.apache.lucene.document.{Document => LuceneDocument}
import org.apache.lucene.document.StringField
import org.apache.lucene.document.Field
import org.apache.lucene.document.TextField
import models.dtos._
import lucene._

object LuceneDocumentService extends LuceneConsts {
  
  def getTextDocument(doc: Document, contents: String) : LuceneDocument = {
    var luceneDocument = new LuceneDocument()
    val documentId = doc.documentId.get
    val documentBoxId = doc.documentBoxId
    luceneDocument.add(new StringField(FIELD_DOC_ID, s"${DOC_TYPE_TEXT}-${documentId}", Field.Store.YES))
    luceneDocument.add(new StringField(FIELD_DOCUMENT_ID, documentId.toString, Field.Store.YES))
    luceneDocument.add(new StringField(FIELD_DOCUMENT_BOX_ID, documentBoxId.toString, Field.Store.YES))
    luceneDocument.add(new StringField(FIELD_DOCUMENT_TYPE, DOC_TYPE_TEXT, Field.Store.YES))
    luceneDocument.add(new Field(FIELD_CONTENTS, contents, highlighterType))
    luceneDocument
  }
  
  def getUserDocument(userId: Long, user: User) : LuceneDocument = {
    var luceneDocument = new LuceneDocument()
    luceneDocument.add(new StringField(FIELD_DOC_ID, s"${DOC_TYPE_USER}-${userId}", Field.Store.YES))
    luceneDocument.add(new StringField(FIELD_USER_ID, userId.toString, Field.Store.YES))
    luceneDocument.add(new StringField(FIELD_DOCUMENT_TYPE, DOC_TYPE_USER, Field.Store.YES))
    luceneDocument.add(new TextField(FIELD_CONTENTS, s"${user.firstName} ${user.lastName} ${user.email}", Field.Store.NO))
    luceneDocument
  }
}