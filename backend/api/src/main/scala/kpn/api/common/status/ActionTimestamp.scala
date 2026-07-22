package kpn.api.common.status

import kpn.api.custom.Timestamp

import java.time.ZonedDateTime
import java.time.temporal.IsoFields

object ActionTimestamp {

  def from(timestamp: Timestamp): ActionTimestamp = {
    fromZoned(timestamp.toLocal)
  }

  def now(): ActionTimestamp = {
    fromZoned(Timestamp.zonedNow)
  }

  def fromZoned(local: ZonedDateTime): ActionTimestamp = {
    ActionTimestamp(
      local.getYear,
      local.getMonthValue,
      local.getDayOfMonth,
      local.getHour,
      local.getMinute,
      local.getSecond,
      local.get(IsoFields.WEEK_BASED_YEAR),
      local.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR),
      local.getDayOfWeek.getValue.toLong
    )
  }

}

case class ActionTimestamp(
  year: Long,
  month: Long,
  day: Long,
  hour: Long,
  minute: Long,
  second: Long,
  weekYear: Long,
  weekWeek: Long,
  weekDay: Long
) {

  def toId: String = {
    f"$year-$month%02d-$day%02d-$hour%02d-$minute%02d-$second%02d"
  }

  def toHuman: String = {
    f"$year-$month%02d-$day%02d $hour%02d:$minute%02d:$second%02d"
  }
}
