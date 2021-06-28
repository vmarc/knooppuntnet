package kpn.api.common.route

import kpn.api.custom.NetworkType

case class RouteNameInfo(
  routeId: Long,
  routeName: String,
  networkType: NetworkType
)
