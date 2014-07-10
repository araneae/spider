package enums

object DocumentType extends BaseEnumeration {
  type DocumentType = Value
  val TEXT, PICTURE, VIDEO = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(DocumentType)
}