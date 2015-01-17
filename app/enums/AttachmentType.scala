package enums

object AttachmentType extends BaseEnumeration {
  type AttachmentType = Value
  val RESUME, COVERLETTER, CERTIFICATE, RECOMMENDATIONLETTER, OTHER = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(AttachmentType)
}