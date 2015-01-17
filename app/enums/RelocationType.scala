package enums

object RelocationType extends BaseEnumeration {
  type RelocationType = Value
  val OK, NOTOK, SOMEWHAT = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(RelocationType)
}