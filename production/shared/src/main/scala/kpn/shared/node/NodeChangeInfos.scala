package kpn.shared.node

case class NodeChangeInfos(
  changes: Seq[NodeChangeInfo],
  incompleteWarning: Boolean,
  more: Boolean
)
