package actors

import models.dtos.Document
import models.dtos.DocumentSearchResult
import models.dtos.User

abstract class Message

// incoming messages to actors
case class MessageAddDocument(document: Document) extends Message

case class MessageDocumentSearch(documentIds: Seq[Long], searchText: String) extends Message

case class MessageDeleteDocument(documentId: Long) extends Message

case class MessageSearchWithHighlighter(documentId: Long, searchText: String) extends Message

//  outgoing messages to actors
case class MessageDocumentSearchResult(documentIds: Seq[Long]) extends Message

case class MessageSearchResultWithHighlighter(documentId: Long, results: Seq[DocumentSearchResult]) extends Message


// user related messages
case class MessageAddUser(user: User) extends Message

case class MessageUserSearch(searchText: String) extends Message

case class MessageUserSearchResult(userIds: List[Long]) extends Message