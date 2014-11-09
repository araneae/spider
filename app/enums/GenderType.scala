package enums

object GenderType extends BaseEnumeration {
  type GenderType = Value
  val MALE, FEMALE, OTHER = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(GenderType)
}