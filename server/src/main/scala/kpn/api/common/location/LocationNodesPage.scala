package kpn.api.common.location

import kpn.api.common.TimeInfo

case class LocationNodesPage(
  timeInfo: TimeInfo,
  summary: LocationSummary,
  nodeCount: Long,
  filter: LocationNodeOptions,
  nodes: Seq[LocationNodeInfo]
)
