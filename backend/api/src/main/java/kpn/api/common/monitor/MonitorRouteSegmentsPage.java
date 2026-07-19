package kpn.api.common.monitor;

import kpn.api.common.monitor.MonitorRouteRelationInfo;
import kpn.api.common.monitor.MonitorRouteSummary;
import kpn.api.common.route.SegmentInfo;

import com.google.common.collect.ImmutableList;

public record MonitorRouteSegmentsPage(
  MonitorRouteSummary summary,
  Long meters,
  ImmutableList<MonitorRouteRelationInfo> relations,
  ImmutableList<SegmentInfo> segments
) {
}

/*
package kpn.api.common.monitor

import kpn.api.common.route.SegmentInfo

case class MonitorRouteSegmentsPage(
  summary: MonitorRouteSummary,
  meters: Long,
  relations: Seq[MonitorRouteRelationInfo],
  segments: Seq[SegmentInfo]
)

*/
