package kpn.api.custom

import kpn.api.common.NetworkScope
import kpn.api.common.RouteType

object ScopedRouteType {

  val rwn: ScopedRouteType = ScopedRouteType(NetworkScope.regional, RouteType.hiking)
  val rcn: ScopedRouteType = ScopedRouteType(NetworkScope.regional, RouteType.cycling)
  val rmn: ScopedRouteType = ScopedRouteType(NetworkScope.regional, RouteType.motorboat)
  val lwn: ScopedRouteType = ScopedRouteType(NetworkScope.local, RouteType.hiking)
  val lcn: ScopedRouteType = ScopedRouteType(NetworkScope.local, RouteType.cycling)
  val lpn: ScopedRouteType = ScopedRouteType(NetworkScope.local, RouteType.canoe)

  def apply(networkScope: NetworkScope, routeType: RouteType): ScopedRouteType = {
    val routeTypeLetter = RouteTypeLetter.letter(routeType)
    val networkScopeLetter = NetworkScopeLetter.letter(networkScope)
    val key = s"$networkScopeLetter${routeTypeLetter}n"
    ScopedRouteType(networkScope, routeType, key)
  }

  val all: Seq[ScopedRouteType] = {
    RouteType.values.flatMap { routeType =>
      NetworkScope.values.map(scope => ScopedRouteType(scope, routeType))
    }
  }

  def withKey(key: String): Option[ScopedRouteType] = {
    all.find(_.key == key)
  }

  def from(networkScope: NetworkScope, routeType: RouteType): ScopedRouteType = {
    all.find(ns => ns.routeType == routeType && ns.networkScope == networkScope).get
  }
}

case class ScopedRouteType(networkScope: NetworkScope, routeType: RouteType, key: String) {

  override def toString: String = key

  def nodeRefTagKey: String = s"${key}_ref"

  def nodeNameTagKey: String = s"${key}_name"

  def proposedNodeRefTagKey: String = s"proposed:${key}_ref"

  def proposedNodeNameTagKey: String = s"proposed:${key}_name"

  def expectedRouteRelationsTag: String = s"expected_${key}_route_relations"
}
