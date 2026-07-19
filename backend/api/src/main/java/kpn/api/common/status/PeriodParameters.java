package kpn.api.common.status;

import java.util.Optional;

public record PeriodParameters(
  String period,
  Long year,
  Optional<Long> month,
  Optional<Long> week,
  Optional<Long> day,
  Optional<Long> hour
) {
}

/*
package kpn.api.common.status

case class PeriodParameters(
  period: String,
  year: Long,
  month: Option[Long],
  week: Option[Long],
  day: Option[Long],
  hour: Option[Long]
)

*/
