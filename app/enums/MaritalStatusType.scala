package enums

object MaritalStatusType extends BaseEnumeration {
  type MaritalStatusType = Value
  val SINGLE, MARRIED, DIVORCED, OTHER = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(MaritalStatusType)
}