package enums

//import scala.slick.driver.JdbcDriver.simple._
import scala.slick.driver.MySQLDriver.simple._

abstract class BaseEnumeration extends Enumeration {

  implicit val EnumTypeColumnMapper = MappedColumnType.base[Value, Int](_.id, this.apply)
  
}