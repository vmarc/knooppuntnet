package kpn.server.api.analysis.pages

import kpn.api.custom.Day
import kpn.core.util.UnitTest

import java.time.ZoneId
import java.time.ZonedDateTime

class SurveyDateInfoBuilderTest extends UnitTest {

  test("dateInfoAt") {
    val dateTime = zonedDateTime(2020, 8, 11)
    val info = SurveyDateInfoBuilder.dateInfoAt(dateTime)

    assert(info.now == day(2020, 8, 11))
    assert(info.lastWeekStart == day(2020, 8, 4))
    assert(info.lastMonthStart == day(2020, 7, 11))
    assert(info.lastHalfYearStart == day(2020, 2, 11))
    assert(info.lastYearStart == day(2019, 8, 11))
    assert(info.lastTwoYearsStart == day(2018, 8, 11))
  }

  private def zonedDateTime(year: Int, month: Int, day: Int): ZonedDateTime = {
    ZonedDateTime.of(year, month, day, 0, 0, 0, 0, ZoneId.of("Europe/Brussels"))
  }

  private def day(year: Int, month: Int, dayOfMonth: Int): Day = {
    Day(year, month, Some(dayOfMonth))
  }
}
