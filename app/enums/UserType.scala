package enums

object UserType extends BaseEnumeration {
  type UserType = Value
  val CANDIDATE, RECRUITER = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(UserType)
}