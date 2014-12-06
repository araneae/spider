package enums

object SalaryTermType extends BaseEnumeration {
  type SalaryTermType = Value
  val WEEK, MONTH, ANNUM = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(SalaryTermType)
}