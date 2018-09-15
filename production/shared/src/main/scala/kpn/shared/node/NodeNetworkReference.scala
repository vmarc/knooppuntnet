package kpn.shared.node

import kpn.shared.NetworkType

case class NodeNetworkReference(
  networkId: Long,
  networkType: NetworkType,
  networkName: String,
  nodeDefinedInRelation: Boolean,
  nodeConnection: Boolean,
  nodeIntegrityCheck: Option[NodeNetworkIntegrityCheck],
  routes: Seq[NodeNetworkRouteReference]
)
