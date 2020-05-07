package kpn.server.api.analysis.pages

import java.time.ZoneId
import java.time.ZonedDateTime

import kpn.api.common.SurveyDateInfo
import kpn.api.custom.Day

object SurveyDateInfoBuilder {

  def dateInfo: SurveyDateInfo = {

    val zoned = ZonedDateTime.now(ZoneId.of("UTC"))
    val local = zoned.withZoneSameInstant(ZoneId.of("Europe/Brussels"))

    val now: Day = Day(
      local.getYear,
      local.getMonthValue,
      Some(local.getDayOfMonth)
    )

    val lastMonthStart: Day = {
      val x = local.toLocalDate.atStartOfDay().minusMonths(1L)
      Day(
        x.getYear,
        x.getMonthValue,
        Some(x.getDayOfMonth)
      )
    }

    val lastHalfYearStart: Day = {
      val x = local.toLocalDate.atStartOfDay().minusMonths(6L)
      Day(
        x.getYear,
        x.getMonthValue,
        Some(x.getDayOfMonth)
      )
    }

    val lastYearStart: Day = {
      val x = local.toLocalDate.atStartOfDay().minusYears(1L)
      Day(
        x.getYear,
        x.getMonthValue,
        Some(x.getDayOfMonth)
      )
    }

    val lastTwoYearsStart: Day = {
      val x = local.toLocalDate.atStartOfDay().minusYears(2L)
      Day(
        x.getYear,
        x.getMonthValue,
        Some(x.getDayOfMonth)
      )
    }

    SurveyDateInfo(
      now,
      lastMonthStart,
      lastHalfYearStart,
      lastYearStart,
      lastTwoYearsStart
    )
  }
}
