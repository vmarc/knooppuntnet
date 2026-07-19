package kpn.api.common.monitor;

import kpn.api.common.monitor.MonitorRouteChangeSummary;

import com.google.common.collect.ImmutableList;

public record MonitorChangesPage(
  Boolean impact,
  Long pageSize,
  Long pageIndex,
  Long totalChangeCount,
  ImmutableList<MonitorRouteChangeSummary> changes
) {
}

/*
package kpn.api.common.monitor

case class MonitorChangesPage(
  impact: Boolean,
  pageSize: Long,
  pageIndex: Long,
  totalChangeCount: Long,
  changes: Seq[MonitorRouteChangeSummary]
)

*/
