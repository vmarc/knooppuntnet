package kpn.api.common

import kpn.api.custom.Day

case class SurveyDateInfo(
  now: Day = Day(0, 1, None),
  lastMonthStart: Day = Day(0, 1, None),
  lastHalfYearStart: Day = Day(0, 1, None),
  lastYearStart: Day = Day(0, 1, None),
  lastTwoYearsStart: Day = Day(0, 1, None)
)
