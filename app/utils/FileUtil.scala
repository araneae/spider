package utils

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption._
import java.io.FileInputStream
import java.security.MessageDigest

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
  
  def createPath(filePath: String) = {
    val path = Paths.get(filePath);
    if (!Files.isDirectory(path)) {
      Files.createDirectories(path)
    }
  }
  
  def copy(srcfilePath: String, tgtFilePath: String) = {
    val source = Paths.get(srcfilePath);
    val target = Paths.get(tgtFilePath);
    Files.copy(source, target, REPLACE_EXISTING)
  }
  
  def delete(filePath: String) = {
    val path = Paths.get(filePath);
    Files.delete(path)
  }
  
  def getMD5Hash(filePath: String): String = {
    val md5 = MessageDigest.getInstance("MD5");
    val fis = new FileInputStream(filePath);
    val dataBytes : Array[Byte] = new Array(1024)
 
    var nread = fis.read(dataBytes)
    while (nread > 0) {
      md5.update(dataBytes, 0, nread)
      nread = fis.read(dataBytes)
    }
    
    md5.digest().map(0xFF & _).map { "%02x".format(_) }.foldLeft(""){_ + _}
  }
}