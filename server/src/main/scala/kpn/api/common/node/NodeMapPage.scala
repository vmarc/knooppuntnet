package kpn.api.common.node

import kpn.api.common.NodeInfo

case class NodeMapPage(
  nodeInfo: NodeInfo,
  changeCount: Long
)
