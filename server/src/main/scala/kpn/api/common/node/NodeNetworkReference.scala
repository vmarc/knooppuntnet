package kpn.api.common.node

import kpn.api.custom.Fact
import kpn.api.custom.NetworkType

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
