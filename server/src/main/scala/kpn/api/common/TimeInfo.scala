package kpn.api.common

import kpn.api.custom.Timestamp

case class TimeInfo(
  now: Timestamp,
  lastWeekStart: Timestamp,
  lastMonthStart: Timestamp,
  lastYearStart: Timestamp
)
