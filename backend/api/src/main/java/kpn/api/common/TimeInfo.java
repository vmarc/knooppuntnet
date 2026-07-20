package kpn.api.common;

import kpn.api.custom.Timestamp;

public record TimeInfo(
  Timestamp now,
  Timestamp lastWeekStart,
  Timestamp lastMonthStart,
  Timestamp lastYearStart
) {
}
