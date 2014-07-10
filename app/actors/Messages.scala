package actors

import models.dtos._

case class MessageDocument(userId: Long, document: Document)

case class MessageSearch(userId: Long, searchText: String)

case class MessageSearchResult(userId: Long, documents: Option[List[Document]])
