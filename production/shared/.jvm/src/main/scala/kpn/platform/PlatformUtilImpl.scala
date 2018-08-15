package kpn.platform

import java.time.ZoneId
import java.time.ZonedDateTime

import kpn.shared.Timestamp

class PlatformUtilImpl extends PlatformUtil {

  override def toLocal(timestamp: Timestamp): Timestamp = {

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
}


