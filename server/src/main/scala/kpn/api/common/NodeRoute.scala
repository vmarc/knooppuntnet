package kpn.api.common

import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType

case class NodeRoute(
  id: Long,
  name: String,
  networkType: NetworkType,
  networkScope: NetworkScope,
  locationNames: Seq[String],
  expectedRouteCount: Int,
  actualRouteCount: Int
) {
  def scopedNetworkType: ScopedNetworkType = {
    ScopedNetworkType.from(networkScope, networkType)
  }
}
