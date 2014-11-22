package enums

object FileType extends BaseEnumeration {
  type FileType = Value
  val DOC, DOCX, TXT, PDF, ODT, RTF, UNKNOWN = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(FileType)
}