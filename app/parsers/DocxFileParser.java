package parsers;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
  
public class DocxFileParser implements FileParser {
 
  @Override
  public String parse(String fileName) throws Exception 
  {
    File file = new File(fileName);
    FileInputStream fis=new FileInputStream(file.getAbsolutePath());
    XWPFDocument hdoc = new XWPFDocument(OPCPackage.open(fis));
    XWPFWordExtractor extractor = new XWPFWordExtractor(hdoc);
    String data = extractor.getText();
    extractor.close();
    
    return (data);
  }
}
