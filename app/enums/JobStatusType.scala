package enums

object JobStatusType extends BaseEnumeration {
  type JobStatusType = Value
  val DRAFT, POSTED, CLOSED = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(JobStatusType)
}