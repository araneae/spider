package parsers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
  
public class TxtFileParser implements FileParser{
 
  @Override
  public String parse(String fileName) throws Exception {
      FileInputStream fileReader = new FileInputStream(fileName);
      InputStreamReader fileInputStream = new InputStreamReader(fileReader, "UTF8");
      BufferedReader reader = new BufferedReader(fileInputStream);
      
      StringBuffer buffer = new StringBuffer();
      
      String line = null;
      while ((line = reader.readLine()) != null) {
        buffer.append(line);
        buffer.append("\n");
      }
      
      reader.close();
      
      return (buffer.toString());
  }  
  
}
