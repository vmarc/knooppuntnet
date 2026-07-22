package kpn.api.common.node

import kpn.api.common.changes.filter.ChangesFilterOption

case class NodeChangesPage(
  nodeId: Long,
  nodeName: String,
  filterOptions: Seq[ChangesFilterOption],
  changes: Seq[NodeChangeInfo],
  totalCount: Long,
  changeCount: Long
)
