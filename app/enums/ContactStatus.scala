package enums

object ContactStatus extends BaseEnumeration {
  type ContactStatus = Value
  val NOTCONNECTED, PENDING, CONNECTED, REJECTED = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(ContactStatus)
}

