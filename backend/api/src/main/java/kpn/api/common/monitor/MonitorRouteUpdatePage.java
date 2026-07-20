package kpn.api.common.monitor;

import kpn.api.common.monitor.MonitorRouteGroup;
import kpn.api.common.monitor.MonitorRouteProperties;

import com.google.common.collect.ImmutableList;

public record MonitorRouteUpdatePage(
  String groupName,
  String groupDescription,
  String routeName,
  String routeDescription,
  ImmutableList<MonitorRouteGroup> groups,
  MonitorRouteProperties properties
) {}

