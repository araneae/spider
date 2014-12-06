package enums

object CompanyUserStatusType extends BaseEnumeration {
  type CompanyUserStatusType = Value
  val INACTIVE, ACTIVE = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(CompanyUserStatusType)
}