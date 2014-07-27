package actors

import models.dtos._

// incoming messages to actors
case class MessageDocument(userId: Long, document: Document)

case class MessageSearch(userId: Long, searchText: String)

case class MessageSearchWithHighlighter(userId: Long, documentId: Long, searchText: String)

//  outgoing messages to actors
case class MessageSearchResult(userId: Long, documents: Option[List[Document]])

case class MessageSearchResultWithHighlighter(userId: Long, documentId: Long, results: Option[List[DocumentSearchResult]])
