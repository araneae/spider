package controllers

import java.io.File
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import actors._
import actors.IndexWriterActor
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout
import enums._
import models.dtos._
import models.repositories._
import play.api.Play
import play.api.Play.current
import play.api.libs.concurrent.Akka
import play.api.libs.json._
import play.api.mvc.Controller
import play.api.mvc.MultipartFormData._
import traits.Secured
import utils._
import play.api.mvc.AsyncResult
import scala.util.Success
import scala.util.Failure
import play.api.libs.concurrent.Execution.Implicits._
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException

object DatabaseController extends Controller with Secured {
  
  private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  // create one lucene actor
  val indexWriterActor = Akka.system.actorOf(Props[IndexWriterActor])
  val indexSearcherActor = Akka.system.actorOf(Props[IndexSearcherActor])
  
  def upload = IsAuthenticated{ username => implicit request =>
      logger.info("in DatabaseController.upload...")
      println("in DatabaseController.upload...")
      val body = request.body.asMultipartFormData
      body match { 
        case Some(maltiPartData) =>
            val filePart = maltiPartData.file("resume")
            filePart match { 
              case Some(file) =>
                    val uploadPath = Configuration.uploadFilePath(userId, file.filename)
                    // check if the file aready exists
                    //val existingDocument = DocumentRepository.findDocument(userId, file.filename)
                    //existingDocument match {
                    //  case Some(doc) => BadRequest(HttpResponseUtil.error("File Aready Exists!"))
                    //  case None => file.ref.moveTo(new File(uploadPath), true)
                    //               Ok(HttpResponseUtil.success("Successfully Uploaded!"))
                    //}
                    file.ref.moveTo(new File(uploadPath), true)
                    Ok(HttpResponseUtil.success("Successfully Uploaded!"))
              case None => BadRequest(HttpResponseUtil.error("Unable to upload file, please try again!"))
            }
       case None => BadRequest(HttpResponseUtil.error("Unable to upload file, please try again!"))
      }
  }
  
  def get(id: Long) = IsAuthenticated{ username => implicit request =>
    logger.info(s"in DatabaseController.get(${id})")
    println(s"in DatabaseController.get(${id})")
    
    val document = DocumentRepository.find(id)
    document match {
        case Some(doc) => val json = Json.toJson(doc)
                          Ok(json).as(JSON)
        case None => BadRequest(HttpResponseUtil.error("Unable to find document!"))
    }
  }
  
  def getAll = IsAuthenticated{ username => implicit request =>
    logger.info("in DatabaseController.getAll...")
    println("in DatabaseController.getAll...")
    
    var list = DocumentRepository.findAll(userId)
    val text = Json.toJson(list)
    Ok(text).as(JSON)
  }

  def create = IsAuthenticated(parse.json){ username => implicit request =>
    logger.info("in DatabaseController.create...")
    println("in DatabaseController.create...")
    try {
      val jsonObj = request.body.asInstanceOf[JsObject]
      val fileName = jsonObj.value.get("fileName").getOrElse(BadRequest("fileName not found")).toString()
      val sanitizedFileName = FileUtil.sanitizeFileName(fileName)
      // inject userId and documentType
      val fileExtension = FileUtil.fileExtension(sanitizedFileName)
      val fileType = FileType.withName(fileExtension.toUpperCase()) 
      val physicalName = TokenGenerator.token
      val documentObj = Json.obj("userId" -> userId) ++ Json.obj("documentType" -> DocumentType.TEXT) ++
                          Json.obj("fileType" -> fileType) ++ Json.obj("physicalName" -> physicalName) ++ jsonObj
      documentObj.validate[Document].fold(
              valid = { document =>
                      // rename the file
                      val filePath = Configuration.uploadFilePath(userId, sanitizedFileName)
                      val newFilePath = Configuration.uploadFilePath(userId, physicalName)
                      // rename the filename
                      FileUtil.move(filePath, newFilePath)
                      val id = DocumentRepository.create(document)
                      // find the saved document
                      val savedDocument = DocumentRepository.find(id)
                      // add the document in the lucene index
                      savedDocument match {
                        case Some(doc) => indexWriterActor ! MessageDocument(userId, doc)
                        case None =>
                      }
                      Ok(HttpResponseUtil.success()).as(JSON)
              },
              invalid = {
                  errors => BadRequest(HttpResponseUtil.error("Something is wrong, please try again!"))
              }
        )
   } catch {
     case error: MySQLIntegrityConstraintViolationException => BadRequest(HttpResponseUtil.error("File Already Exists!")).as(JSON)
     case error: Throwable => BadRequest(HttpResponseUtil.error("Something is wrong, please try again!")).as(JSON)
   }
  }

  def update(id: Int) = IsAuthenticated{ username => implicit request =>
    logger.info("in DatabaseController.update...")
    println("in DatabaseController.update...")
    val json = request.body.asInstanceOf[JsObject]
    json.validate[Document].fold(
          valid = { Document =>
                  DocumentRepository.udate(Document)
                  Ok(HttpResponseUtil.success("Successfully updated!"))
          },
          invalid = {
              errors => BadRequest(HttpResponseUtil.error("Something is wrong, please try again!"))
          }
    )
  }

  def search(searchText: String) = IsAuthenticated{ username => implicit request =>
    logger.info(s"in DatabaseController.search(${searchText})...")
    println(s"in DatabaseController.search(${searchText})...")
    
    if (searchText.length() > 0){
      AsyncResult {
          implicit val timeout = Timeout(5000)
          // send message to index searcher
          val f = ask(indexSearcherActor, MessageSearch(userId, searchText)).mapTo[MessageSearchResult]
          f.map{
               case result : MessageSearchResult => {
                    result.documents match {
                      case Some(list) =>
                          val text = Json.toJson(list)
                          Ok(text).as(JSON)
                      case None => 
                          Ok("").as(JSON)
                    }
                }
//                case Failure(failure) =>
//                        println(s"Failrure ${failure}")
//                        Ok("")
          }
      }
    }
    else {
      Ok("")
    }
  }
  
  def delete(id: Long) = IsAuthenticated{ username => implicit request =>
    Ok("")
  }
}