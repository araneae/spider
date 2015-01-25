package enums

object SearchQueryType extends BaseEnumeration {
  type SearchQueryType = Value
  val MYDOCUMENTS = Value
  
  implicit val enumTypeFormat = EnumUtils.enumFormat(SearchQueryType)
}

