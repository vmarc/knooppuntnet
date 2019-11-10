package kpn.api.common.node

case class NodeChangeInfos(
  changes: Seq[NodeChangeInfo],
  incompleteWarning: Boolean
)
