package enums

object CompanyStatusType extends BaseEnumeration {
  type CompanyStatusType = Value
  val INACTIVE, ACTIVE = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(CompanyStatusType)
}