package enums

object CompanyUserType extends BaseEnumeration {
  type CompanyUserType = Value
  val SUPERUSER, ADMIN, USER = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(CompanyUserType)
}