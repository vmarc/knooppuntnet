package kpn.api.common.monitor;

import kpn.api.common.monitor.MonitorRouteChangeSummary;

import com.google.common.collect.ImmutableList;

public record MonitorGroupChangesPage(
  String groupName,
  String groupDescription,
  Boolean impact,
  Long pageSize,
  Long pageIndex,
  Long totalChangeCount,
  ImmutableList<MonitorRouteChangeSummary> changes
) {
}

/*
package kpn.api.common.monitor

case class MonitorGroupChangesPage(
  groupName: String,
  groupDescription: String,
  impact: Boolean,
  pageSize: Long,
  pageIndex: Long,
  totalChangeCount: Long,
  changes: Seq[MonitorRouteChangeSummary]
)

*/
