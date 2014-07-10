package enums

object ContactStatus extends BaseEnumeration {
  type ContactStatus = Value
  val PENDING, REJECTED, CONNECTED = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(ContactStatus)
}

