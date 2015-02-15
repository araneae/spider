package utils

import org.junit.Test
import java.io._
import org.xml.sax._
import edu.stanford.nlp.ie.crf.CRFClassifier
import edu.stanford.nlp.ling.CoreAnnotations
import parsers._

class EntityParserTest {

  @Test 
  def reponseTest = {
     EntityParser.parse
  }

}
 
object EntityParser {
  
  def parse = {
    val file = """/Users/Coupang/Documents/others/resumes/BubulBaruah.pdf"""
    //val file = """/Users/Coupang/Documents/others/resumes/Kothandaraman Sikamani.doc"""
   
    val parser = TikaFileParser.getInstance
    val contents = parser.parse(file)
    println(contents)
   
    val src = "/Users/Coupang/Downloads/stanford-ner-2013-06-20/classifiers/"
    val classifier1 = "english.all.3class.distsim.crf.ser.gz"
    val classifier2 = "english.conll.4class.distsim.crf.ser.gz"
    val classifier3 = "english.muc.7class.distsim.crf.ser.gz"
   
    val serializedClassifier = src + classifier1
   
    val classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier)
    val out = classifier.classify(contents)
   
    var words = 0
    for (i <- 0 to out.size() - 1) {
      val sentence = out.get(i)
   
      var foundWord = ""
      var oldWordClass = ""
   
      for (j <- 0 to sentence.size() - 1) {
        val word = sentence.get(j)
        val wordClass = word.get(classOf[CoreAnnotations.AnswerAnnotation]) + ""
   
        if (!oldWordClass.equals(wordClass)) {
          if (!oldWordClass.equals("O") && !oldWordClass.equals("")) {
            print("[/" + oldWordClass + "]")
          }
        }
   
        if (!wordClass.equals("O") && !wordClass.equals("")) {
          if (!oldWordClass.equals(wordClass)) {
            print("[" + wordClass + "]")
          }
        }
   
        oldWordClass = wordClass
   
        words = words + 1
        print(word);
        print(" ");
   
        if (words > 10) {
          words = 0
          println(" ")
        }
      }
    }
  }
}