package kpn.api.common.location

import kpn.api.common.TimeInfo

case class LocationNodesPage(
  timeInfo: TimeInfo,
  summary: LocationSummary,
  nodeCount: Long,
  allNodeCount: Long,
  integrityCheckFailedNodeCount: Long,
  filter: LocationNodeOptions,
  nodes: Seq[LocationNodeInfo]
)
