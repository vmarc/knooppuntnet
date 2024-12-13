package kpn.api.common.route

import kpn.api.common.NetworkType

case class RouteNameInfo(
  routeId: Long,
  routeName: String,
  networkType: NetworkType
)
