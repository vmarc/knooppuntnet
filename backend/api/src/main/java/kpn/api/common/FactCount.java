package kpn.api.common;

import kpn.api.common.Fact;

public record FactCount(
  Fact fact,
  Long count
) {
}
