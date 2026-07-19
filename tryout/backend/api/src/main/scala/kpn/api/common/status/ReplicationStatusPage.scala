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
