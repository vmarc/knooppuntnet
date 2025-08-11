package kpn.api.common

import kpn.api.custom.Day

case class SurveyDateInfo(
  now: Day,
  lastWeekStart: Day,
  lastMonthStart: Day,
  lastHalfYearStart: Day,
  lastYearStart: Day,
  lastTwoYearsStart: Day
)
