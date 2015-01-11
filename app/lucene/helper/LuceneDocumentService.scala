package lucene.helper

import org.apache.lucene.document.{Document => LuceneDocument}
import org.apache.lucene.document.StringField
import org.apache.lucene.document.Field
import org.apache.lucene.document.TextField
import org.apache.lucene.document.DoubleField
import models.dtos._
import lucene._

object LuceneDocumentService extends LuceneConsts {
  
  def getTextDocument(doc: Document, contents: String) : LuceneDocument = {
    var luceneDocument = new LuceneDocument()
    val documentId = doc.documentId.get
    val documentFolderId = doc.documentFolderId
    luceneDocument.add(new StringField(FIELD_DOC_ID, s"${DOC_TYPE_TEXT}-${documentId}", Field.Store.YES))
    luceneDocument.add(new StringField(FIELD_DOCUMENT_ID, documentId.toString, Field.Store.YES))
    luceneDocument.add(new StringField(FIELD_DOCUMENT_FOLDER_ID, documentFolderId.toString, Field.Store.YES))
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
  
  def getJobDocument(company: Company, jobRequirement: JobRequirement, jobRequirementXtn: JobRequirementXtn) : LuceneDocument = {
    var luceneDocument = new LuceneDocument()
    val jobRequirementId = jobRequirement.jobRequirementId.get
    luceneDocument.add(new StringField(FIELD_DOC_ID, s"${DOC_TYPE_JOB}-${jobRequirementId}", Field.Store.YES))
    luceneDocument.add(new StringField(FIELD_JOB_ID, s"${jobRequirementId}", Field.Store.YES))
    luceneDocument.add(new StringField(FIELD_DOCUMENT_TYPE, DOC_TYPE_JOB, Field.Store.YES))
    luceneDocument.add(new StringField(FIELD_JOB_COMPANY_ID, s"${jobRequirement.companyId}", Field.Store.YES))
    luceneDocument.add(new StringField(FIELD_JOB_STATUS, s"${jobRequirement.status}", Field.Store.NO))
    luceneDocument.add(new TextField(FIELD_JOB_LOCATION, jobRequirement.location, Field.Store.NO))
    jobRequirementXtn.locationLat match {
      case Some(locationLat) =>
        luceneDocument.add(new DoubleField(FIELD_JOB_LOCATION_LAT, locationLat, Field.Store.NO))
      case None =>
    }
    jobRequirementXtn.locationLng match {
      case Some(locationLng) =>
        luceneDocument.add(new DoubleField(FIELD_JOB_LOCATION_LNG, locationLng, Field.Store.NO))
      case None =>
    }
    luceneDocument.add(new TextField(FIELD_JOB_COMPANY_NAME, company.name, Field.Store.NO))
    luceneDocument.add(new TextField(FIELD_JOB_TITLE, jobRequirement.title, Field.Store.NO))
    luceneDocument.add(new TextField(FIELD_JOB_POST_DATE, s"${jobRequirement.postDate}", Field.Store.NO))
    luceneDocument.add(new TextField(FIELD_JOB_DESCRIPTION, jobRequirement.description, Field.Store.NO))
    luceneDocument.add(new TextField(FIELD_CONTENTS, s"${jobRequirement.title} ${company.name} ${jobRequirement.description}", Field.Store.NO))
    luceneDocument
  }
}