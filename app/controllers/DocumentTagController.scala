package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import models.dtos._
import models.repositories.DocumentTagRepository
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Controller
import traits.Secured
import utils.HttpResponseUtil
import org.joda.time.DateTime
import models.repositories.UserDocumentRepository

object DocumentTagController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def getAll(documentId: Int) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DocumentTagController.getAll(${documentId})")
    println(s"in DocumentTagController.getAll(${documentId})")
    
    var list = DocumentTagRepository.getAll(userId, documentId)
    val text = Json.toJson(list)
    Ok(text).as(JSON)
  }

  def create(documentId: Int) = IsAuthenticated(parse.json){ username => implicit request =>
    //logger.info(s"in DocumentTagController.create(${documentId})")
    println(s"in DocumentTagController.create(${documentId})")
    
    val jsonObj = request.body.asInstanceOf[JsObject]
    jsonObj.validate[DocumentTagDTO].fold(
      valid = { documentTagDTO =>
                  val optUserDocument = DocumentTagRepository.findByUserTagIdDocumentId(documentTagDTO.userTagId, documentTagDTO.documentId)
                  optUserDocument match {
                    case None =>
                      val documentTag = DocumentTag(userId, documentTagDTO, userId, new DateTime, None, None)
                      DocumentTagRepository.create(documentTag)
                      Ok(HttpResponseUtil.success("Successfully created tag!"))
                    case Some(userDocument) => 
                      BadRequest(HttpResponseUtil.error("Document tag already exists!"))
            }
          },
      invalid = {
          errors => BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
      }
    )
  }

  def delete(documentId: Int, userTagId: Int) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DocumentTagController.delete(${documentId}, ${userTagId})")
    println(s"in DocumentTagController.delete(${documentId}, ${userTagId})")
    DocumentTagRepository.delete(userId, documentId, userTagId)
    Ok(HttpResponseUtil.success("Successfully deleted tag!"))
  }

}