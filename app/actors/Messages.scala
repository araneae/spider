package actors

import models.dtos._

abstract class Message

// incoming messages to actors
case class MessageAddDocument(document: Document) extends Message

case class MessageDocumentSearch(documentFolderIds: Seq[Long], searchText: String) extends Message

case class MessageDeleteDocument(documentId: Long) extends Message

case class MessageSearchWithHighlighter(documentId: Long, searchText: String) extends Message

case class MessageDocumentGetContents(documentIds: Long) extends Message

//  outgoing messages from actors
case class MessageDocumentSearchResult(documentIds: Seq[Long]) extends Message

case class MessageSearchResultWithHighlighter(documentId: Long, results: Seq[DocumentSearchResult]) extends Message

case class MessageDocumentContents(documentId: Long, contents: String) extends Message

// user related messages
case class MessageAddUser(user: User) extends Message

case class MessageUserSearch(searchText: String) extends Message

case class MessageUserSearchResult(userIds: List[Long]) extends Message

// jobs related

case class MessageAddJob(company: Company, jobRequirement: JobRequirement, jobRequirementXtn: JobRequirementXtn) extends Message

case class MessageJobSearch(jobSearchDTO: JobSearchDTO) extends Message

case class MessageJobSearchResult(jobRequirementIds: List[Long]) extends Message
