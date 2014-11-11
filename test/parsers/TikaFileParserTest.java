package parsers;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;

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
	  //String fileName = "/tika/AnilNarayanapillai.docx";
	  //String fileName = "/tika/Play Framework Essentials — Sample.pdf";
	  String fileName = "/tika/Robert_Kohr_Resume.rtf";
	  //String fileName = "/tika/woodcock_resume.rtf";
	  
	  TikaFileParser parser = TikaFileParser.getInstance();
	  String data = parser.parse(fileName);
	  assertNotNull("Unable to parse file", data);
	  
	  System.out.println(data);
  }  
  
  //@Test
  public void testTika() throws Exception {
	  //InputStream input=new FileInputStream(new File("/tika/AnilNarayanapillai.docx"));
	  InputStream input=new FileInputStream(new File("/tika/Robert_Kohr_Resume.rtf"));
	  ContentHandler textHandler= new BodyContentHandler();
	  Metadata metadata=new Metadata();
	  Parser parser=new AutoDetectParser();
	  ParseContext context=new ParseContext();
	  parser.parse(input,textHandler, metadata, context);
	  System.out.println("Title: " + metadata.get(Metadata.TITLE));
	  System.out.println("Body: " + textHandler.toString());
	}
  
  //@Test
  public void testRtf() throws IOException, BadLocationException
  {
	  //InputStream stream=new FileInputStream(new File("/tika/Robert_Kohr_Resume.rtf"));
	  InputStream stream=new FileInputStream(new File("/tika/woodcock_resume.rtf"));
      RTFEditorKit kit = new RTFEditorKit();  
      Document doc = kit.createDefaultDocument();  
      kit.read(stream, doc, 0);  
 
      String plainText = doc.getText(0, doc.getLength());  
      System.out.println("text" + plainText);  
  }
}
