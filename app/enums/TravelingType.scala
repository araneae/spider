package enums

object TravelingType extends BaseEnumeration {
  type TravelingType = Value
  val OK, NOTOK, SOMEWHAT = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(TravelingType)
}