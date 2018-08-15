package kpn.shared

case class TimeInfo(
  now: Timestamp = Timestamp(0, 0, 0, 0, 0, 0),
  lastWeekStart: Timestamp = Timestamp(0, 0, 0, 0, 0, 0),
  lastMonthStart: Timestamp = Timestamp(0, 0, 0, 0, 0, 0),
  lastYearStart: Timestamp = Timestamp(0, 0, 0, 0, 0, 0)
)
