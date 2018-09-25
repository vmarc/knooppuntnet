package kpn.shared.node

import kpn.shared.Fact
import kpn.shared.NetworkType

case class NodeNetworkReference(
  networkType: NetworkType,
  networkId: Long,
  networkName: String,
  nodeDefinedInRelation: Boolean,
  nodeConnection: Boolean,
  nodeRoleConnection: Boolean,
  nodeIntegrityCheck: Option[NodeNetworkIntegrityCheck],
  facts: Seq[Fact],
  routes: Seq[NodeNetworkRouteReference]
)
