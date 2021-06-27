package kpn.api.common.node

import kpn.api.common.changes.filter.ChangesFilter

case class NodeChangesPage(
  nodeId: Long,
  nodeName: String,
  filter: ChangesFilter,
  changes: Seq[NodeChangeInfo],
  incompleteWarning: Boolean,
  totalCount: Long,
  changeCount: Long
)
