package enums

object JobStatusType extends BaseEnumeration {
  type JobStatusType = Value
  val DRAFT, PUBLISHED, CLOSED = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(JobStatusType)
}