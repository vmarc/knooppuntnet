package kpn.shared.node

import kpn.shared.NodeInfo

case class NodeChangesPage(
  nodeInfo: NodeInfo,
  changes: Seq[NodeChangeInfo],
  incompleteWarning: Boolean,
  totalCount: Int
)
