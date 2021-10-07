package kpn.core.common

import kpn.api.custom.Timestamp

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Reflects a point in time in the UTC timezone.
 *
 * Allows control over the <i>current time</i> in the
 * current execution thread (used during unit testing time dependent logic).
 *
 * If no specific time point is set, the normal system time is in effect.
 */
object TimestampUtil {

  def toLocal(timestamp: Timestamp): Timestamp = {

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

    val local = zoned.withZoneSameInstant(ZoneId.of("Europe/Brussels"))

    Timestamp(
      local.getYear,
      local.getMonthValue,
      local.getDayOfMonth,
      local.getHour,
      local.getMinute,
      local.getSecond
    )
  }

  def toKey(timestamp: Timestamp): String = {
    s"${timestamp.yearString}${timestamp.monthString}${timestamp.dayString}${timestamp.hourString}${timestamp.minuteString}${timestamp.secondString}"
  }

  def relativeSeconds(timestamp: Timestamp, seconds: Int): Timestamp = {
    val local = toLocalDateTime(timestamp)
    val result = if (seconds > 0) {
      local.plusSeconds(seconds.toLong)
    }
    else {
      local.minusSeconds(-seconds.toLong)
    }
    Timestamp(result.getYear, result.getMonthValue, result.getDayOfMonth, result.getHour, result.getMinute, result.getSecond)
  }

  def cacheDir(t: Timestamp): String = {
    s"${t.yearString}/${t.monthString}/${t.dayString}/${t.hourString}/${t.minuteString}/${t.secondString}"
  }

  def parseIso(string: String): Timestamp = {
    if (string.length == "2020-08-11 12:34:56".length && string(10) == ' ') {
      // this was added to support the migration of timestamps in the monitor documents
      // TODO MONGO can be removed after mongodb migration is complete
      val modified = string.replaceAll(" ", "T") + "Z"
      toTimestamp(ZonedDateTime.parse(modified + "[UTC]", DateTimeFormatter.ISO_DATE_TIME))
    }
    else {
      toTimestamp(ZonedDateTime.parse(string + "[UTC]", DateTimeFormatter.ISO_DATE_TIME))
    }
  }

  def fromMilliSeconds(milliSeconds: Long): Timestamp = {
    val instant = Instant.ofEpochMilli(milliSeconds)
    val zoned = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"))
    toTimestamp(zoned)
  }

  def toTimestamp(zoned: ZonedDateTime): Timestamp = {
    Timestamp(
      zoned.getYear,
      zoned.getMonthValue,
      zoned.getDayOfMonth,
      zoned.getHour,
      zoned.getMinute,
      zoned.getSecond
    )
  }

  private def toLocalDateTime(timestamp: Timestamp): LocalDateTime = {
    LocalDateTime.of(
      timestamp.year,
      timestamp.month,
      timestamp.day,
      timestamp.hour,
      timestamp.minute,
      timestamp.second
    )
  }
}
