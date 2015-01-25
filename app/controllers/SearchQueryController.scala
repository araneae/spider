package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import models.dtos.SearchQuery
import models.repositories.SearchQueryRepository
import play.api.libs.json.JsObject
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.mvc.Controller
import traits.Secured
import utils.HttpResponseUtil
import org.joda.time.DateTime

object SearchQueryController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def getAll = IsAuthenticated{ username => implicit request =>
    //logger.info("in SearchQueryController.getAll()")
    println("in SearchQueryController.getAll()")
    
    var list = SearchQueryRepository.findAll(userId)
    val text = Json.toJson(list)
    Ok(text).as(JSON)
  }

  def create = IsAuthenticated(parse.json){ username => implicit request =>
    //logger.info(s"in SearchQueryController.create()")
    println(s"in SearchQueryController.create()")
    val jsonObj = request.body.asInstanceOf[JsObject]
    val documentSearchObj = Json.obj("userId" -> userId) ++ Json.obj("createdUserId" -> userId) ++ 
                            Json.obj("createdAt" -> new DateTime()) ++ jsonObj
    documentSearchObj.validate[SearchQuery].fold(
          valid = { documentSearch =>
                  SearchQueryRepository.create(documentSearch)
                  Ok(HttpResponseUtil.success("Successfully created search text!"))
          },
          invalid = {
              errors => BadRequest(HttpResponseUtil.error("Unable to parse payload!"))
          }
    )
  }
  
  def update(documentSearchId: Long) = IsAuthenticated(parse.json){ username => implicit request =>
    //logger.info(s"in SearchQueryController.update(${documentSearchId})")
    println(s"in SearchQueryController.update(${documentSearchId})")
    val json = request.body.asInstanceOf[JsObject]
    json.validate[SearchQuery].fold(
          valid = { SearchQuery =>
                  SearchQueryRepository.udate(SearchQuery)
                  Ok(HttpResponseUtil.success("Successfully updated!"))
          },
          invalid = {
              errors => BadRequest(HttpResponseUtil.error("Something is wrong, please try again!"))
          }
    )
  }

  def delete(documentSearchId: Long) = IsAuthenticated{ username => implicit request =>
    //logger.info(s"in SearchQueryController.delete(${documentSearchId})")
    println(s"in SearchQueryController.delete(${documentSearchId})")
    SearchQueryRepository.delete(documentSearchId)
    Ok(HttpResponseUtil.success("Successfully deleted search text!"))
  }

}