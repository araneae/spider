package services

import enums.FileType._
import parsers._

object FileParserService {
 
   def parse(fileType: FileType, fileName:String) : Option[String] = {
     fileType match {
       case DOC => {
                       //val parser = new DocFileParser()
                       val parser = TikaFileParser.getInstance
                       Some(parser.parse(fileName))
                    }
       case DOCX => {
                       //val parser = new DocxFileParser()
                       val parser = TikaFileParser.getInstance
                       Some(parser.parse(fileName))
                    }
       case PDF => {
                       //val parser = new DocxFileParser()
                       val parser = TikaFileParser.getInstance
                       Some(parser.parse(fileName))
                    }
       case RTF => {
                       val parser = RTFFileParser.getInstance()
                       Some(parser.parse(fileName))
                    }
       case TXT => {
                       val parser = new TxtFileParser()
                       Some(parser.parse(fileName))
                    }
       case _ =>    { // not supported yet
                       None
                     }
       }
   }
}