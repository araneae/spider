
import play.api.libs.json._
import play.api.libs.functional.syntax._
import enums._
import scala.collection.mutable.ArrayBuffer
//import enums.UserType._

object Status extends Enumeration {
    type Status = Value
    val Enabled, Disabled = Value
}

import Status._
case class Product(id: Option[Long], ean: Long, name: String, description: String, status: Status = Status.Enabled)
//case class Product(id: Long, ean: Long, name: String, description: String)

// Now let's test it
object EnumTest {
  implicit val enumTypeFormat = EnumUtils.enumFormat(Status)
  
    val productWrites : Writes[Product] = (
          (JsPath \ "id").write[Option[Long]] and
          (JsPath \ "ean").write[Long] and
          (JsPath \ "name").write[String] and
          (JsPath \ "description").write[String] and
          (JsPath \ "status").write[Status]
    )(unlift(Product.unapply))

    val productReads : Reads[Product] = (
        (JsPath \ "id").readNullable[Long] and
        (JsPath \ "ean").read[Long] and
        (JsPath \ "name").read[String] and
        (JsPath \ "description").read[String] and
        (JsPath \ "status").read[Status]
    )(Product)
    
 
  def main(args: Array[String]): Unit = {
      object EnumA extends Enumeration {
      type EnumA = Value
      val VAL1, VAL2, VAL3 = Value
      }
       
      implicit val enumAFormat = EnumUtils.enumFormat(EnumA)
      val myEnumJson: JsValue = Json.toJson(EnumA.VAL1)
      
      println(myEnumJson);
      //val myValue: EnumA.Value = myEnumJson.asOpt[EnumA].getOrElse(sys.error("Oh noes! Invalid value!"))
      
      //println(myValue);
      //def ok = EnumA.VAL1 == myValue
      
      
      println("SkillLevel.values "+SkillLevel.values);
      var b= new StringBuilder
      for (d <- SkillLevel.values){
        val item = new StringContext(",{ \"name\":\"", "\"}").s(d.toString())
        b ++= item
      }
      b.deleteCharAt(0);
      b.insert(0, "[")
      b ++= "]";
      println(b.toString);
  }
}

