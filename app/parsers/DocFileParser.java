package parsers;

import java.io.FileInputStream;

import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.HeaderStories;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
  
public class DocFileParser implements FileParser{
 
  @Override
  public String parse(String fileName) throws Exception {
      POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(fileName));
      HWPFDocument doc = new HWPFDocument(fs);
      StringBuffer buffer = new StringBuffer();
 
      buffer.append(readParagraphs(doc));
 
      return (buffer.toString());
  }  
 
  private String readParagraphs(HWPFDocument doc) throws Exception {
    StringBuffer buffer = new StringBuffer();
    
    WordExtractor we = new WordExtractor(doc);
 
    //Get the total number of paragraphs
    String[] paragraphs = we.getParagraphText();
    for (int i = 0; i < paragraphs.length; i++) {
      buffer.append(paragraphs[i].toString());
    }
    we.close();
    
    return (buffer.toString());
  }
 
  @SuppressWarnings("unused")
  private String readHeader(HWPFDocument doc, int pageNumber) {
      HeaderStories headerStore = new HeaderStories( doc);
      String header = headerStore.getHeader(pageNumber);
      return (header);
  }
 
  @SuppressWarnings("unused")
  private String readFooter(HWPFDocument doc, int pageNumber) {
      HeaderStories headerStore = new HeaderStories( doc);
      String footer = headerStore.getFooter(pageNumber);
      return(footer);
  }
 
  @SuppressWarnings("unused")
  private String readDocumentSummary(HWPFDocument doc) {
    StringBuffer buffer = new StringBuffer();
    DocumentSummaryInformation summaryInfo=doc.getDocumentSummaryInformation();
    String category = summaryInfo.getCategory();
    String company = summaryInfo.getCompany();
    int lineCount=summaryInfo.getLineCount();
    int sectionCount=summaryInfo.getSectionCount();
    int slideCount=summaryInfo.getSlideCount();
     
    buffer.append("Category: "+category);
    buffer.append("Company: "+company);
    buffer.append("Line Count: "+lineCount);
    buffer.append("Section Count: "+sectionCount);
    buffer.append("Slide Count: "+slideCount);
    
    return (buffer.toString());
  }
}
