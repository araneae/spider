package parsers;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.text.Document;
import javax.swing.text.rtf.RTFEditorKit;
  
public class RTFFileParser implements FileParser{
 
  @Override
  public String parse(String fileName) throws Exception {
	  InputStream stream=new FileInputStream(fileName);
      RTFEditorKit kit = new RTFEditorKit();
      Document doc = kit.createDefaultDocument();
      kit.read(stream, doc, 0);
      String text = doc.getText(0, doc.getLength());
      stream.close();
      return (text);
  }
  
  public static RTFFileParser getInstance() {
	  return (new RTFFileParser());
  }
}
