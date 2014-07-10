package utils

import scala.util.parsing.json.JSONObject

object HttpResponseUtil {

  def success(message: String = "OK", data: String = "[]") = {
    reponseSuccess(message, data)
  }
  
  def error(message: String) = {
    reponseError(message)
  }
  
  def reponseSuccess(message: String, data: String) = {
    s"""{"message":"${message}", "data":"${data}"}"""
  }
  
  def reponseError(message: String) = {
    s"""{"message":"${message}"}"""
  }

}