package kpn.api.common.location

import kpn.api.common.TimeInfo

case class LocationNodesPage(
  timeInfo: TimeInfo,
  summary: LocationSummary,
  nodeCount: Long,
  allNodeCount: Long,
  factsNodeCount: Long,
  surveyNodeCount: Long,
  integrityCheckFailedNodeCount: Long,
  nodes: Seq[LocationNodeInfo]
)
