package enums

object CurrencyType extends BaseEnumeration {
  type CurrencyType = Value
  val USD, CAD = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(CurrencyType)
}