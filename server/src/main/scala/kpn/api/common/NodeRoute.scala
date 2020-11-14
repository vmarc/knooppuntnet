package kpn.api.common

import kpn.api.custom.NetworkType

case class NodeRoute(
  id: Long,
  name: String,
  networkType: NetworkType,
  locationNames: Seq[String],
  expectedRouteCount: Int,
  actualRouteCount: Int
)
