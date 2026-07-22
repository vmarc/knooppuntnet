package kpn.core.doc

import kpn.api.common.RouteType
import kpn.api.id.WithStringId

case class NodeNetworkRef(
  _id: String,
  nodeId: Long,
  networkId: Long,
  routeType: RouteType,
  networkName: String
) extends WithStringId
