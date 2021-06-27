package kpn.api.common.node

import kpn.api.common.NodeInfo
import kpn.api.common.common.Reference

case class NodeDetailsPage(
  nodeInfo: NodeInfo,
  mixedNetworkScopes: Boolean,
  routeReferences: Seq[Reference],
  networkReferences: Seq[Reference],
  integrity: Option[NodeIntegrity],
  changeCount: Long
)
