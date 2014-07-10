package enums

object MessageBoxType extends BaseEnumeration {
  type MessageBoxType = Value
  val INBOX, DRAFT, OUTBOX, SENTITEMS, CUSTOM = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(MessageBoxType)
}