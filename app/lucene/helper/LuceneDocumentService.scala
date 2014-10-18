package lucene.helper

import org.apache.lucene.document._
import models.dtos.User
import lucene._

object LuceneDocumentService extends LuceneConsts {
  
  def getTextDocument(docId: Long, contents: String) : Document = {
    var luceneDocument = new Document()
    luceneDocument.add(new StringField(FIELD_DOC_ID, s"${DOC_TYPE_TEXT}-${docId}", Field.Store.YES))
    luceneDocument.add(new StringField(FIELD_DOCUMENT_ID, docId.toString, Field.Store.YES))
    luceneDocument.add(new StringField(FIELD_DOCUMENT_TYPE, DOC_TYPE_TEXT, Field.Store.YES))
    luceneDocument.add(new Field(FIELD_CONTENTS, contents, highlighterType))
    luceneDocument
  }
  
  def getUserDocument(userId: Long, user: User) : Document = {
    var luceneDocument = new Document()
    luceneDocument.add(new StringField(FIELD_DOC_ID, s"${DOC_TYPE_USER}-${userId}", Field.Store.YES))
    luceneDocument.add(new StringField(FIELD_USER_ID, userId.toString, Field.Store.YES))
    luceneDocument.add(new StringField(FIELD_DOCUMENT_TYPE, DOC_TYPE_USER, Field.Store.YES))
    luceneDocument.add(new TextField(FIELD_CONTENTS, s"${user.firstName} ${user.lastName} ${user.email}", Field.Store.NO))
    luceneDocument
  }
}