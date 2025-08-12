package kpn.core.doc

import kpn.api.common.RouteType

case class RouteNetworkRef(
  _id: String,
  routeId: Long,
  networkId: Long,
  routeType: RouteType,
  networkName: String
) extends WithStringId
