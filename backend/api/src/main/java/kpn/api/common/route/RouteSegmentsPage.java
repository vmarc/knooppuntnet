package kpn.api.common.route;

import kpn.api.common.route.RouteInfo;
import kpn.api.common.route.SegmentInfo;

import com.google.common.collect.ImmutableList;

public record RouteSegmentsPage(
  RouteInfo routeInfo,
  ImmutableList<SegmentInfo> segments
) {}
