package kpn.shared.node

import kpn.shared.NodeInfo

case class NodeDetailsPage(
  nodeInfo: NodeInfo,
  references: NodeReferences,
  changeCount: Int
)
