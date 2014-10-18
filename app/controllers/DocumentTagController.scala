package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import models.dtos.DocumentTag
import models.repositories.DocumentTagRepository
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Controller
import traits.Secured
import utils.HttpResponseUtil
import org.joda.time.DateTime

object DocumentTagController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def getAll(documentId: Int) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in DocumentTagController.getAll(${documentId})")
    println(s"in DocumentTagController.getAll(${documentId})")
    
    var list = DocumentTagRepository.findAll(userId, documentId)
    val text = Json.toJson(list)
    Ok(text).as(JSON)
  }

  def create(documentId: Int) = IsAuthenticated(parse.json){ username => implicit request =>
    //logger.info(s"in DocumentTagController.create(${documentId})")
    println(s"in DocumentTagController.create(${documentId})")
    val jsonObj = request.body.asInstanceOf[JsObject]
    // merge userId with the request object
    val userTagObj = Json.obj("userId" -> userId) ++ Json.obj("createdUserId" -> userId) ++
                     Json.obj("createdAt" -> new DateTime()) ++ jsonObj
    userTagObj.validate[DocumentTag].fold(
          valid = { documentTag =>
                  DocumentTagRepository.create(documentTag)
                  Ok(HttpResponseUtil.success("Successfully created tag!"))
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