package kpn.api.common.node

import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.common.Ref
import kpn.api.custom.ScopedRouteType

case class NodeIntegrityDetail(
  routeType: RouteType,
  routeScope: RouteScope,
  expectedRouteCount: Int,
  routeRefs: Seq[Ref]
) {

  def failed: Boolean = {
    routeRefs.sizeIs != expectedRouteCount
  }

  def hasScopedRouteType(scopedRouteType: ScopedRouteType): Boolean = {
    scopedRouteType.routeType == routeType && scopedRouteType.routeScope == routeScope
  }
}
