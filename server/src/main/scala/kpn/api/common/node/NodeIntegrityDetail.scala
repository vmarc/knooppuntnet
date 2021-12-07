package kpn.api.common.node

import kpn.api.common.common.Ref
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType

case class NodeIntegrityDetail(
  networkType: NetworkType,
  networkScope: NetworkScope,
  expectedRouteCount: Int,
  routeRefs: Seq[Ref]
) {

  def failed: Boolean = {
    routeRefs.size != expectedRouteCount
  }

  def hasScopedNetworkType(scopedNetworkType: ScopedNetworkType): Boolean = {
    scopedNetworkType.networkType == networkType && scopedNetworkType.networkScope == networkScope
  }
}
