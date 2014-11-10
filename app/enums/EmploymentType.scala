package enums

object EmploymentType extends BaseEnumeration {
  type EmploymentType = Value
  val CONTRACT, CONTRACTTOHIRE, FULLTIME, INTERNHIRE, PARTTIME, TEMP = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(EmploymentType)
}