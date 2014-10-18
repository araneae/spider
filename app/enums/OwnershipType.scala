package enums

object OwnershipType extends BaseEnumeration {
  type OwnershipType = Value
  val OWNED, SHARED = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(OwnershipType)
}

