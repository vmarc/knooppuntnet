package kpn.core.mongo

import kpn.api.base.WithStringId
import kpn.api.custom.NetworkType

case class RouteNetworkRef(
  _id: String,
  routeId: Long,
  networkId: Long,
  networkType: NetworkType,
  networkName: String
) extends WithStringId
