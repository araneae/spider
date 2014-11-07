package utils

object HtmlUtil {

  def sanitize(data: Array[String]): Array[String] = {
      data.map{ line => 
           sanitize(line)
      }
  }
  
  def sanitize(contents: String): String = {
      contents.replaceAll("\r\n", "<br/>")
               .replaceAll("\n", "<br/>")
               .replaceAll("\r", "")
               .replaceAll("\u0007", "  ")
               .replaceAll("\t", "  ")
  }

}