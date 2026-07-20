package kpn.api.common.monitor;

import kpn.api.common.monitor.MonitorRouteChangeSummary;

import com.google.common.collect.ImmutableList;

public record MonitorChangesPage(
  Boolean impact,
  Long pageSize,
  Long pageIndex,
  Long totalChangeCount,
  ImmutableList<MonitorRouteChangeSummary> changes
) {}

