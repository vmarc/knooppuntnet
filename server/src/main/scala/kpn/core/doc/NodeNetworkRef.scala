package kpn.core.doc

import kpn.api.base.WithStringId
import kpn.api.common.RouteType

case class NodeNetworkRef(
  _id: String,
  nodeId: Long,
  networkId: Long,
  routeType: RouteType,
  networkName: String
) extends WithStringId
