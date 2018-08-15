package kpn.core.engine.changes

case class ChangeSetAnalysis(
  diffCount: Int,
  networkCount: Int,
  removedNetworkNodesCount: Int,
  addedNetworkNodesCount: Int,
  updatedNetworkNodesCount: Int,
  removedRoutesCount: Int,
  addedRoutesCount: Int,
  updatedRoutesCount: Int,
  happy: Boolean,
  investigate: Boolean
)
