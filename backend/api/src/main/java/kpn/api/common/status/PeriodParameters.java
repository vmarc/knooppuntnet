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
