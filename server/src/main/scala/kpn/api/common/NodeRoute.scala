package kpn.api.common

import kpn.api.custom.NetworkType

case class NodeRoute(
  id: Long,
  networkType: NetworkType,
  locationNames: Seq[String],
  expectedRouteCount: Int,
  actualRouteCount: Int
)
