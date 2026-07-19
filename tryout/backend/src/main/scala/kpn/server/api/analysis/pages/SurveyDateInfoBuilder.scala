package kpn.server.api.analysis.pages

import kpn.api.common.SurveyDateInfo
import kpn.api.custom.Day

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

object SurveyDateInfoBuilder {

  def dateInfo: SurveyDateInfo = {
    val zoned = ZonedDateTime.now(ZoneId.of("UTC"))
    val local = zoned.withZoneSameInstant(ZoneId.of("Europe/Brussels"))
    dateInfoAt(local)
  }

  def dateInfoAt(local: ZonedDateTime): SurveyDateInfo = {
    val startOfDay = local.toLocalDate.atStartOfDay()
    SurveyDateInfo(
      now = timeToDay(startOfDay),
      lastWeekStart = timeToDay(startOfDay.minusWeeks(1)),
      lastMonthStart = timeToDay(startOfDay.minusMonths(1)),
      lastHalfYearStart = timeToDay(startOfDay.minusMonths(6)),
      lastYearStart = timeToDay(startOfDay.minusYears(1)),
      lastTwoYearsStart = timeToDay(startOfDay.minusYears(2))
    )
  }

  private def timeToDay(time: LocalDateTime) = {
    Day(
      time.getYear,
      time.getMonthValue,
      Some(time.getDayOfMonth)
    )
  }
}
