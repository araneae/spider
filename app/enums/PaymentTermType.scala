package enums

object PaymentTermType extends BaseEnumeration {
  type PaymentTermType = Value
  val HOURLY, DAILY, WEEKLY, MONTHLY, ANNUALLY = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(PaymentTermType)
}