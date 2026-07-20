package kpn.api.common.monitor;

import kpn.api.common.changes.details.ChangeKey;

import java.util.Optional;

public record MonitorRouteChangeSummary(
  ChangeKey key,
  Optional<String> groupName,
  Optional<String> routeName,
  Optional<String> groupDescription,
  Optional<String> comment,
  Long wayCount,
  Long waysAdded,
  Long waysRemoved,
  Long waysUpdated,
  Long osmDistance,
  Long routeSegmentCount,
  Long newNokSegmentCount,
  Long resolvedNokSegmentCount,
  Boolean happy,
  Boolean investigate
) {}
