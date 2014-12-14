package enums

object SalaryType extends BaseEnumeration {
  type SalaryType = Value
  val MINMAX, OPEN, COMPETITIVE, DOE = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(SalaryType)
}