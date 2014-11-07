package parsers;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.junit.Test;
import org.xml.sax.ContentHandler;


  
public class TikaFileParserTest {
 
  //@Test
  public void testParse() throws Exception {
	  //String fileName = "/Users/Coupang/Documents/others/tikka/AnilNarayanapillai.docx";
	  String fileName = "/Users/Coupang/Documents/others/tikka/Play Framework Essentials — Sample.pdf";
	  
	  TikaFileParser parser = TikaFileParser.getInstance();
	  String data = parser.parse(fileName);
	  assertNotNull("Unable to parse file", data);
	  
	  System.out.println(data);
  }  
  
  //@Test
  public void testTika() throws Exception {
	  InputStream input=new FileInputStream(new File("/Users/Coupang/Documents/others/tikka/AnilNarayanapillai.docx"));
	  ContentHandler textHandler= new BodyContentHandler();
	  Metadata metadata=new Metadata();
	  Parser parser=new AutoDetectParser();
	  ParseContext context=new ParseContext();
	  parser.parse(input,textHandler, metadata, context);
	  System.out.println("Title: " + metadata.get(Metadata.TITLE));
	  System.out.println("Body: " + textHandler.toString());
	}
}
