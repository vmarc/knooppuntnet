package kpn.api.common.subset

import kpn.api.common.NodeInfo
import kpn.api.common.TimeInfo

case class SubsetOrphanNodesPage(
  timeInfo: TimeInfo,
  subsetInfo: SubsetInfo,
  rows: Seq[NodeInfo]
)
