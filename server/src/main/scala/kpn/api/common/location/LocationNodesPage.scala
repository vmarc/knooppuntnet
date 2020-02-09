package kpn.api.common.location

import kpn.api.common.TimeInfo

case class LocationNodesPage(
  timeInfo: TimeInfo,
  summary: LocationSummary,
  nodes: Seq[LocationNodeInfo]
)
