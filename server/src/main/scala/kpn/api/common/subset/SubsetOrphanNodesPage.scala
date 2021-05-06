package kpn.api.common.subset

import kpn.api.common.OrphanNodeInfo
import kpn.api.common.TimeInfo

case class SubsetOrphanNodesPage(
  timeInfo: TimeInfo,
  subsetInfo: SubsetInfo,
  nodes: Seq[OrphanNodeInfo]
)
