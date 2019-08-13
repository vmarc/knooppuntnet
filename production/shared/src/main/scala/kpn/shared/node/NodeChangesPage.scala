package kpn.shared.node

import kpn.shared.NodeInfo
import kpn.shared.changes.filter.ChangesFilter

case class NodeChangesPage(
  nodeInfo: NodeInfo,
  filter: ChangesFilter,
  changes: Seq[NodeChangeInfo],
  incompleteWarning: Boolean,
  totalCount: Int
)
