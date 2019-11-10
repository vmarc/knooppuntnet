package kpn.api.common.node

import kpn.api.common.NodeInfo
import kpn.api.common.changes.filter.ChangesFilter

case class NodeChangesPage(
  nodeInfo: NodeInfo,
  filter: ChangesFilter,
  changes: Seq[NodeChangeInfo],
  incompleteWarning: Boolean,
  totalCount: Int,
  changeCount: Int
)
