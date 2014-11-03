package utils

import scala.util.Random

object TokenGenerator {

  def token = java.util.UUID.randomUUID.toString
  
  def otp: String = {
      var number = 0
      val digits = random(4, 8)
      for( a <- 1 to digits ) {
         number = number * 10 + random(0, 9)
      }
      number.toString()
  }
  
  def random(min: Int, max: Int): Int = {
    val random = new Random()
    random.nextInt(max - min + 1) + min
  }
}