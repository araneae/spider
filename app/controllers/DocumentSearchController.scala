package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import models.dtos.DocumentSearch
import models.repositories.DocumentSearchRepository
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Controller
import traits.Secured
import utils.HttpResponseUtil

object DocumentSearchController extends Controller with Secured {
  
  private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def getAll = IsAuthenticated{ username => implicit request =>
    logger.info("in DocumentSearchController.getAll()")
    println("in DocumentSearchController.getAll()")
    
    var list = DocumentSearchRepository.findAll(userId)
    val text = Json.toJson(list)
    Ok(text).as(JSON)
  }

  def create = IsAuthenticated(parse.json){ username => implicit request =>
    logger.info(s"in DocumentSearchController.create()")
    println(s"in DocumentSearchController.create()")
    val jsonObj = request.body.asInstanceOf[JsObject]
    // merge userId with the request object
    val userIdObj = Json.obj("userId" -> userId)
    val documentSearchObj = userIdObj ++ jsonObj
    documentSearchObj.validate[DocumentSearch].fold(
          valid = { documentSearch =>
                  DocumentSearchRepository.create(documentSearch)
                  Ok(HttpResponseUtil.success("Successfully created search text!"))
          },
          invalid = {
              errors => BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
          }
    )
  }
  
  def update(id: Int) = IsAuthenticated(parse.json){ username => implicit request =>
    logger.info(s"in DocumentSearchController.update(${id})")
    println(s"in DocumentSearchController.update(${id})")
    val json = request.body.asInstanceOf[JsObject]
    json.validate[DocumentSearch].fold(
          valid = { DocumentSearch =>
                  DocumentSearchRepository.udate(DocumentSearch)
                  Ok(HttpResponseUtil.success("Successfully updated!"))
          },
          invalid = {
              errors => BadRequest(HttpResponseUtil.error("Something is wrong, please try again!"))
          }
    )
  }

  def delete(id: Int) = IsAuthenticated{ username => implicit request =>
    logger.info(s"in DocumentSearchController.delete(${id})")
    println(s"in DocumentSearchController.delete(${id})")
    DocumentSearchRepository.delete(id)
    Ok(HttpResponseUtil.success("Successfully deleted search text!"))
  }

}