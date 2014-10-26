package utils

object HtmlUtil {

  def sanitize(data: Array[String]): Array[String] = {
      data.map{ line => 
           sanitize(line)
      }
  }
  
  def sanitize(contents: String): String = {
      val temp = contents.replaceAll("\r\n", "<br/>")
      temp.replaceAll("\u0007", "\t")
  }

}