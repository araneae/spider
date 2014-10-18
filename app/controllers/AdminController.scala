package controllers

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import play.api.mvc.Controller
import traits.Secured
import models._

object AdminController extends Controller with Secured {
  
  //private final val logger: Logger = LoggerFactory.getLogger(classOf[Application])
  
  def admin = IsAuthenticated { username => _ =>
    //logger.info("in AdminController.index...")
    println("in index...")
    Ok(views.html.admin("Back Office Admin Module"))
  }
  
}