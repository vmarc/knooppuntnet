package kpn.api.common.location;

import kpn.api.common.TimeInfo;
import kpn.api.common.location.LocationNodeInfo;
import kpn.api.common.location.LocationNodeOptions;
import kpn.api.common.location.LocationSummary;

import com.google.common.collect.ImmutableList;

public record LocationNodesPage(
  TimeInfo timeInfo,
  LocationSummary summary,
  Long nodeCount,
  LocationNodeOptions filter,
  ImmutableList<LocationNodeInfo> nodes
) {
}
