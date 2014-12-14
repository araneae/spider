package enums

object BackgroundCheckType extends BaseEnumeration {
  type BackgroundCheckType = Value
  val NONE, STANDARD, SECURITYCLEARANCE = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(BackgroundCheckType)
}