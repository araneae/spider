package models.dtos

import play.api.libs.functional.syntax._
import play.api.libs.json._
import enums.DocumentType._
import enums.FileType._
import org.joda.time.DateTime

/**
 * Document Tag DTO.
 * 
 */
case class DocumentTagDTO(userTagId: Long,
                          documentId: Long,
                          userDocumentId: Long) {
  def this(documentTag: DocumentTag) {
      this(documentTag.userTagId,
           documentTag.documentId,
           documentTag.userDocumentId)
  }
}

object DocumentTagDTO extends Function3[Long, Long, Long, DocumentTagDTO]
{
    implicit val documentTagWrites : Writes[DocumentTagDTO] = (
            (JsPath \ "userTagId").write[Long] and
            (JsPath \ "documentId").write[Long] and
            (JsPath \ "userDocumentId").write[Long]
    )(unlift(DocumentTagDTO.unapply))

    implicit val documentTagReads : Reads[DocumentTagDTO] = (
          (JsPath \ "userTagId").read[Long] and
          (JsPath \ "documentId").read[Long] and
          (JsPath \ "userDocumentId").read[Long]
    )(DocumentTagDTO)
    
    def apply(documentTag: DocumentTag) = new DocumentTagDTO(documentTag)
}
