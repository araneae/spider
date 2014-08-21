package enums

object MessageBoxType extends BaseEnumeration {
  type MessageBoxType = Value
  val INBOX, SENTITEMS, TRASH, CUSTOM = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(MessageBoxType)
}