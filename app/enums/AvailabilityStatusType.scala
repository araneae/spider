package enums

object AvailabilityStatusType extends BaseEnumeration {
  type AvailabilityStatusType = Value
  val NOTAVAILABLE, LOOKING, ACTIVELYLOOKING, AVAILABLEINFUTURE = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(AvailabilityStatusType)
}