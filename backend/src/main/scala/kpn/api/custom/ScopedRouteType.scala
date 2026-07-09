package kpn.api.custom

import kpn.api.common.RouteScope
import kpn.api.common.RouteType

object ScopedRouteType {

  val rwn: ScopedRouteType = ScopedRouteType(RouteType.hiking, RouteScope.regional)
  val rcn: ScopedRouteType = ScopedRouteType(RouteType.cycling, RouteScope.regional)
  val rmn: ScopedRouteType = ScopedRouteType(RouteType.motorboat, RouteScope.regional)
  val lwn: ScopedRouteType = ScopedRouteType(RouteType.hiking, RouteScope.local)
  val lcn: ScopedRouteType = ScopedRouteType(RouteType.cycling, RouteScope.local)
  val lpn: ScopedRouteType = ScopedRouteType(RouteType.canoe, RouteScope.local)

  def apply(routeType: RouteType, routeScope: RouteScope): ScopedRouteType = {
    val routeTypeLetter = RouteTypeLetter.letter(routeType)
    val routeScopeLetter = RouteScopeLetter.letter(routeScope)
    val key = s"$routeScopeLetter${routeTypeLetter}n"
    ScopedRouteType(routeType, routeScope, key)
  }

  val all: Seq[ScopedRouteType] = {
    RouteType.values.flatMap { routeType =>
      RouteScope.all.map(scope => ScopedRouteType(routeType, scope))
    }
  }

  def withKey(key: String): Option[ScopedRouteType] = {
    all.find(_.key == key)
  }

  def from(routeType: RouteType, routeScope: RouteScope): ScopedRouteType = {
    all.find(ns => ns.routeType == routeType && ns.routeScope == routeScope).get
  }
}

case class ScopedRouteType(routeType: RouteType, routeScope: RouteScope, key: String) {

  override def toString: String = key

  def nodeRefTagKey: String = s"${key}_ref"

  def nodeNameTagKey: String = s"${key}_name"

  def proposedNodeRefTagKey: String = s"proposed:${key}_ref"

  def proposedNodeNameTagKey: String = s"proposed:${key}_name"

  def expectedRouteRelationsTag: String = s"expected_${key}_route_relations"
}
