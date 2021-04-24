package kpn.api.common

import kpn.api.custom.ScopedNetworkType

case class NodeRoute(
  id: Long,
  name: String,
  scopedNetworkType: ScopedNetworkType,
  locationNames: Seq[String],
  expectedRouteCount: Int,
  actualRouteCount: Int
)
