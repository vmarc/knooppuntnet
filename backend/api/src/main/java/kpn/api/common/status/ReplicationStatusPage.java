package kpn.api.common.status;

import kpn.api.common.status.ActionTimestamp;
import kpn.api.common.status.BarChart;
import kpn.api.common.status.BarChart2D;

public record ReplicationStatusPage(
  ActionTimestamp timestamp,
  String periodType,
  String periodTitle,
  String previous,
  String next,
  BarChart2D delay,
  BarChart analysisDelay,
  BarChart updateDelay,
  BarChart replicationDelay,
  BarChart replicationBytes,
  BarChart replicationElements,
  BarChart replicationChangeSets
) {
}

/*
package kpn.api.common.status

case class ReplicationStatusPage(
  timestamp: ActionTimestamp,
  periodType: String,
  periodTitle: String,
  previous: String,
  next: String,
  delay: BarChart2D,
  analysisDelay: BarChart,
  updateDelay: BarChart,
  replicationDelay: BarChart,
  replicationBytes: BarChart,
  replicationElements: BarChart,
  replicationChangeSets: BarChart
)

*/
