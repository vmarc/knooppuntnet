package kpn.api.common;

import kpn.api.common.Fact;
import kpn.api.custom.Timestamp;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record OrphanRouteInfo(
  Long id,
  String name,
  Long meters,
  Optional<String> lastSurvey,
  Timestamp lastUpdated,
  ImmutableList<Fact> facts,
  Boolean investigate
) {
}
