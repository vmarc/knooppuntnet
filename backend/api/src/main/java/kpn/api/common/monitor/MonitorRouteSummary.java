package kpn.api.common.monitor;

import kpn.api.common.Bounds;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record MonitorRouteSummary(
  Boolean adminUser,
  String groupName,
  String routeName,
  String routeDescription,
  String routeId,
  Optional<Long> relationId,
  ImmutableList<Long> relationIds,
  Long memberCount,
  Long segmentCount,
  Long deviationCount,
  Optional<Bounds> bounds
) {}

