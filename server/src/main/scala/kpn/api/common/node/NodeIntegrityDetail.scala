package kpn.api.common.node

import kpn.api.common.NetworkScope
import kpn.api.common.NetworkType
import kpn.api.common.common.Ref
import kpn.api.custom.ScopedNetworkType

case class NodeIntegrityDetail(
  networkType: NetworkType,
  networkScope: NetworkScope,
  expectedRouteCount: Int,
  routeRefs: Seq[Ref]
) {

  def failed: Boolean = {
    routeRefs.sizeIs != expectedRouteCount
  }

  def hasScopedNetworkType(scopedNetworkType: ScopedNetworkType): Boolean = {
    scopedNetworkType.networkType == networkType && scopedNetworkType.networkScope == networkScope
  }
}
