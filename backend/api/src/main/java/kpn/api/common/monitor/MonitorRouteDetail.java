package kpn.api.common.monitor;

import kpn.api.common.Bounds;
import kpn.api.custom.Timestamp;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record MonitorRouteDetail(
  Long rowIndex,
  String routeId,
  String name,
  String description,
  Optional<String> symbol,
  Optional<Long> relationId,
  String referenceType,
  Optional<Timestamp> referenceTimestamp,
  Long referenceDistance,
  Long deviationDistance,
  Long deviationCount,
  Long osmSegmentCount,
  ImmutableList<Long> relationIds,
  Optional<Bounds> bounds,
  Boolean happy
) {}
