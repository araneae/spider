package enums

object SalarayTermType extends BaseEnumeration {
  type SalarayTermType = Value
  val WEEK, MONTH, ANNUM = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(SalarayTermType)
}