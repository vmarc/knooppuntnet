package kpn.api.common

import kpn.api.custom.Timestamp

case class TimeInfo(
  now: Timestamp = Timestamp(0, 1, 1, 0, 0, 0),
  lastWeekStart: Timestamp = Timestamp(0, 1, 1, 0, 0, 0),
  lastMonthStart: Timestamp = Timestamp(0, 1, 1, 0, 0, 0),
  lastYearStart: Timestamp = Timestamp(0, 1, 1, 0, 0, 0)
)
