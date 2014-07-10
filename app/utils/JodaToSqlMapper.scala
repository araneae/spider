package utils

import org.joda.time.DateTime
import java.sql.Timestamp
import play.api.db.slick.Config.driver.simple._

object JodaToSqlMapper {
  implicit val dateTimeToDate = MappedColumnType.base[DateTime, Timestamp] (
             dateTime => new Timestamp(dateTime.getMillis),
             timestamp => new DateTime(timestamp))
}