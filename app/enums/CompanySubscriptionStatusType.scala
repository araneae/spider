package enums

object CompanySubscriptionStatusType extends BaseEnumeration {
  type CompanySubscriptionStatusType = Value
  val PENDING, ACTIVE, SUSPENDED, INACTIVE = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(CompanySubscriptionStatusType)
}