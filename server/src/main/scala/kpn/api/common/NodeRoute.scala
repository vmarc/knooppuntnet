package kpn.api.common

import kpn.api.custom.NetworkType

case class NodeRoute(
  id: Long,
  networkType: NetworkType,
  actualRouteCount: Int,
  expectedRouteCount: Option[Int]
)
