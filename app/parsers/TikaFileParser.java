package parsers;

import java.io.File;

import org.apache.tika.Tika;


  
public class TikaFileParser implements FileParser {
 
  @Override
  public String parse(String fileName) throws Exception {
	  Tika tikka = new Tika();
      String data = tikka.parseToString(new File(fileName));
      return (data);
  }
  
  public static TikaFileParser getInstance() {
	  return (new TikaFileParser());
  }
  
}
