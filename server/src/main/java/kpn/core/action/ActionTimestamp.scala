package kpn.core.action

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.IsoFields

import kpn.api.custom.Timestamp
import kpn.core.common.Time

object ActionTimestamp {

  private val localZone = ZoneId.of("Europe/Brussels")

  def from(timestamp: Timestamp): ActionTimestamp = {
    toActionTimestamp(toLocal(timestamp))
  }

  def now(): ActionTimestamp = {
    toActionTimestamp(ZonedDateTime.now(localZone))
  }

  def minuteDiffInfo(id: Long, timestamp: Timestamp): MinuteDiffInfo = {
    val localTimestamp = toLocal(timestamp)
    val localNow = toLocal(Time.now)
    val delay = (localNow.toInstant.toEpochMilli - localTimestamp.toInstant.toEpochMilli) / 1000

    MinuteDiffInfo(
      id,
      toActionTimestamp(localTimestamp),
      toActionTimestamp(localNow),
      delay
    )
  }

  private def toLocal(timestamp: Timestamp): ZonedDateTime = {
    val zoned = ZonedDateTime.of(
      timestamp.year,
      timestamp.month,
      timestamp.day,
      timestamp.hour,
      timestamp.minute,
      timestamp.second,
      0,
      ZoneId.of("UTC")
    )
    zoned.withZoneSameInstant(localZone)
  }

  private def toActionTimestamp(local: ZonedDateTime): ActionTimestamp = {
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

}
