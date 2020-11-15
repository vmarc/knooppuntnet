package kpn.api.common.node

import kpn.api.common.NodeInfo

case class NodeDetailsPage(
  nodeInfo: NodeInfo,
  references: NodeReferences,
  integrity: NodeIntegrity,
  changeCount: Long
)
