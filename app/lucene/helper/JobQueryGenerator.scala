package lucene.helper

import org.apache.lucene.queryparser.complexPhrase.ComplexPhraseQueryParser
import org.apache.lucene.search.BooleanQuery
import scala.collection.mutable.ListBuffer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.search._
import org.apache.lucene.util._
import org.apache.lucene.index.Term
import lucene._
import models.dtos._
import enums.JobStatusType

object JobQueryGenerator extends LuceneConsts {
  
  val analyzer = new StandardAnalyzer(Version.LUCENE_47)
  val parserJobTitle = new ComplexPhraseQueryParser(Version.LUCENE_47, FIELD_JOB_TITLE, analyzer)
  val parserCompany = new ComplexPhraseQueryParser(Version.LUCENE_47, FIELD_JOB_COMPANY_NAME, analyzer)
  val parserContents = new ComplexPhraseQueryParser(Version.LUCENE_47, FIELD_CONTENTS, analyzer)
  
  def getQuery(jobSearchDTO: JobSearchDTO) : Query = {
    val booleanQuery = new BooleanQuery()
   
    jobSearchDTO.company match {
      case Some(company) =>
        val query = parserCompany.parse(company.toUpperCase)
        booleanQuery.add(query, BooleanClause.Occur.SHOULD)
      case None =>
    }
   
    jobSearchDTO.title match {
      case Some(title) =>
        val query = parserJobTitle.parse(title.toUpperCase)
        booleanQuery.add(query, BooleanClause.Occur.SHOULD)
      case None =>
    }
    
    jobSearchDTO.contents match {
      case Some(contents) =>
        val query = parserContents.parse(contents.toUpperCase)
        booleanQuery.add(query, BooleanClause.Occur.SHOULD)
      case None =>
    }
    
    booleanQuery
  }
  
  def getFilter() : Filter = {
    val booleanQuery = new BooleanQuery();
    
    val term = new TermQuery(new Term(FIELD_DOCUMENT_TYPE, DOC_TYPE_JOB))
    booleanQuery.add(term, BooleanClause.Occur.MUST)
    
    val termStatus = new TermQuery(new Term(FIELD_JOB_STATUS, s"${JobStatusType.POSTED}"))
    booleanQuery.add(termStatus, BooleanClause.Occur.MUST)
    
    val filter = new QueryWrapperFilter(booleanQuery)
    filter
  }

}