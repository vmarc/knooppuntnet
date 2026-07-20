package kpn.api.common.monitor;

import java.util.Optional;

public record MonitorRouteSubRelation(
  Optional<Long> subRelationIndex,
  Long relationId,
  String name
) {
}
