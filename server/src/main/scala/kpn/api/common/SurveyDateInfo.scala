package kpn.api.common

import kpn.api.custom.Day

case class SurveyDateInfo(
  now: Day = Day(0, 1),
  lastMonthStart: Day = Day(0, 1),
  lastHalfYearStart: Day = Day(0, 1),
  lastYearStart: Day = Day(0, 1),
  lastTwoYearsStart: Day = Day(0, 1)
)
