package kpn.api.common;

import kpn.api.custom.Timestamp;

import java.util.Optional;

public record OrphanNodeInfo(
  Long id,
  String name,
  Optional<String> longName,
  Timestamp lastUpdated,
  Boolean proposed,
  Optional<String> lastSurvey,
  Long factCount
) {
}
