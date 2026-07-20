package kpn.api.common;

import kpn.api.custom.Day;

public record SurveyDateInfo(
  Day now,
  Day lastWeekStart,
  Day lastMonthStart,
  Day lastHalfYearStart,
  Day lastYearStart,
  Day lastTwoYearsStart
) {
}
