package utils

import scala.util.parsing.json.JSONObject

object HtmleUtil {

  def sanitize(data: Array[String]): Array[String] = {
      data.map{ line => 
                val temp = line.replaceAll("\r\n", "<br/>")
                temp.replaceAll("\u0007", "\t")
      }
  }

}