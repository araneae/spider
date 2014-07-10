package utils

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object FileUtil {

  def sanitizeFileName(fileName: String): String = {
      fileName.filter(c => c != '\"');
  }
  
  def fileExtension(fileName: String): String = {
      val index = fileName.lastIndexOf('.')
      fileName.substring(index + 1)
  }
  
  def move(filePath: String, newFilePath: String) = {
    val oldName = Paths.get(filePath);
    val newName = Paths.get(newFilePath);
    Files.move(oldName, newName)
  }
  
  def delete(filePath : String) = {
    val path = Paths.get(filePath);
    Files.delete(path)
  }
}