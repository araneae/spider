package enums

object WorkStatusType extends BaseEnumeration {
  type WorkStatusType = Value
  val NOTWORKING, STUDENT, WORKING, WORKINGFORME, RETIRED = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(WorkStatusType)
}