package kpn.core.facade.pages

import java.time.ZoneId
import java.time.ZonedDateTime

import kpn.shared.TimeInfo
import kpn.shared.Timestamp

object TimeInfoBuilder {

  def timeInfo: TimeInfo = {

    val zoned = ZonedDateTime.now(ZoneId.of("UTC"))
    val local = zoned.withZoneSameInstant(ZoneId.of("Europe/Brussels"))

    val now: Timestamp = Timestamp(
      local.getYear,
      local.getMonthValue,
      local.getDayOfMonth,
      local.getHour,
      local.getMinute,
      local.getSecond
    )

    val lastWeekStart: Timestamp = {
      val x = local.toLocalDate.atStartOfDay().minusWeeks(1L)
      Timestamp(
        x.getYear,
        x.getMonthValue,
        x.getDayOfMonth,
        x.getHour,
        x.getMinute,
        x.getSecond
      )
    }

    val lastMonthStart: Timestamp = {
      val x = local.toLocalDate.atStartOfDay().minusMonths(1L)
      Timestamp(
        x.getYear,
        x.getMonthValue,
        x.getDayOfMonth,
        x.getHour,
        x.getMinute,
        x.getSecond
      )
    }

    val lastYearStart: Timestamp = {
      val x = local.toLocalDate.atStartOfDay().minusYears(1L)
      Timestamp(
        x.getYear,
        x.getMonthValue,
        x.getDayOfMonth,
        x.getHour,
        x.getMinute,
        x.getSecond
      )
    }

    TimeInfo(
      now,
      lastWeekStart,
      lastMonthStart,
      lastYearStart
    )
  }

}
