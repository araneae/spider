package enums

object EducationLevelType extends BaseEnumeration {
  type EducationLevelType = Value
  val HIGHSCHOOL, GRADUATE, POSTGRADUATE, DOCTORATE = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(EducationLevelType)
}