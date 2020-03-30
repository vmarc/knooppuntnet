package kpn.api.common.node

import kpn.api.common.NodeMapInfo

case class NodeMapPage(
  nodeMapInfo: NodeMapInfo,
  changeCount: Long
)
