package kpn.shared.node

import kpn.shared.NetworkType

case class NodeNetworkReference(
  networkType: NetworkType,
  networkId: Long,
  networkName: String,
  nodeDefinedInRelation: Boolean,
  nodeRoleConnection: Boolean,
  nodeIntegrityCheck: Option[NodeNetworkIntegrityCheck],
  routes: Seq[NodeNetworkRouteReference]
)
