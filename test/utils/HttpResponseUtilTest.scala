package utils

import org.junit.Test
import org.junit.Assert._

class HttpResponseUtilTest {

  @Test 
  def reponseTest = {
    val text = HttpResponseUtil.success("OK", "[]")
    val expected = """{"message":"OK", "data":"[]"}"""
    assertEquals(expected, text)
  }
}