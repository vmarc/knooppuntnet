package kpn.shared.subset

import kpn.shared.NodeInfo
import kpn.shared.TimeInfo

case class SubsetOrphanNodesPage(
  timeInfo: TimeInfo,
  subsetInfo: SubsetInfo,
  rows: Seq[NodeInfo]
)
