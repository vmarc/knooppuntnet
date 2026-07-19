package kpn.api.common.monitor;

import kpn.api.common.monitor.MonitorRouteDeviationInfo;
import kpn.api.common.monitor.MonitorRouteSummary;

import com.google.common.collect.ImmutableList;

public record MonitorRouteDeviationsPage(
  MonitorRouteSummary summary,
  Long deviationDistance,
  ImmutableList<MonitorRouteDeviationInfo> deviations
) {
}

/*
package kpn.api.common.monitor

case class MonitorRouteDeviationsPage(
  summary: MonitorRouteSummary,
  deviationDistance: Long,
  deviations: Seq[MonitorRouteDeviationInfo]
)

*/
