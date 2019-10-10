package kpn.shared.node

import kpn.shared.NodeInfo

case class NodePage(
  nodeInfo: NodeInfo,
  references: NodeReferences,
  nodeChanges: NodeChangeInfos
)
