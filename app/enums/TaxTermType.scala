package enums

object TaxTermType extends BaseEnumeration {
  type TaxTermType = Value
  val W2, TEN99, C2C = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(TaxTermType)
}