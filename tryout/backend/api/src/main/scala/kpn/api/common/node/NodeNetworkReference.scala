package kpn.api.common.node

import kpn.api.common.Fact
import kpn.api.common.RouteType

case class NodeNetworkReference(
  routeType: RouteType,
  networkId: Long,
  networkName: String,
  nodeDefinedInRelation: Boolean,
  nodeConnection: Boolean,
  nodeRoleConnection: Boolean,
  nodeIntegrityCheck: Option[NodeNetworkIntegrityCheck],
  facts: Seq[Fact],
  routes: Seq[NodeNetworkRouteReference]
)
