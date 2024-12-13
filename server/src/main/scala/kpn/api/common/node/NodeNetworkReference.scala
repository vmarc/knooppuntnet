package kpn.api.common.node

import kpn.api.common.NetworkType
import kpn.api.custom.Fact

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
