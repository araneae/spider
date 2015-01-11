package enums

object UserStatusType extends BaseEnumeration {
  type UserStatusType = Value
  val PENDING, ACTIVE, LOCKED, DEACTIVATED = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(UserStatusType)
}