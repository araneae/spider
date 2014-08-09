package utils

import scala.util.parsing.json.JSONObject

object HttpResponseUtil {
  private final val EMPTY_OBJ = "{}"
  private final val EMPTY_LIST = "[]"

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

  def reponseEmptyObject() = {
    EMPTY_OBJ
  }
  
  def reponseEmptyList() = {
    EMPTY_LIST
  }
}