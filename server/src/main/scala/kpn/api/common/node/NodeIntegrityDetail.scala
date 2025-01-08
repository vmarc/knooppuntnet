package kpn.api.common.node

import kpn.api.common.NetworkScope
import kpn.api.common.RouteType
import kpn.api.common.common.Ref
import kpn.api.custom.ScopedRouteType

case class NodeIntegrityDetail(
  routeType: RouteType,
  networkScope: NetworkScope,
  expectedRouteCount: Int,
  routeRefs: Seq[Ref]
) {

  def failed: Boolean = {
    routeRefs.sizeIs != expectedRouteCount
  }

  def hasScopedRouteType(scopedRouteType: ScopedRouteType): Boolean = {
    scopedRouteType.routeType == routeType && scopedRouteType.networkScope == networkScope
  }
}
