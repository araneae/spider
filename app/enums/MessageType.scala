package enums

object MessageType extends BaseEnumeration {
  type MessageType = Value
  val IN, OUT, DRAFT = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(MessageType)
}