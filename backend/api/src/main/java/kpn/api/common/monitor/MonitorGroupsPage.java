package kpn.api.common.monitor;

import kpn.api.common.monitor.MonitorGroupsPageGroup;

import com.google.common.collect.ImmutableList;

public record MonitorGroupsPage(
  Boolean adminUser,
  Long routeCount,
  ImmutableList<MonitorGroupsPageGroup> groups
) {
}
