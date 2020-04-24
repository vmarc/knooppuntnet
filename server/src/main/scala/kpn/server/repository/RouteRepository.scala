package kpn.server.repository

import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteReferences

trait RouteRepository {

  def allRouteIds(): Seq[Long]

  def save(routes: RouteInfo): Unit

  def delete(routeIds: Seq[Long]): Unit

  def routeWithId(routeId: Long): Option[RouteInfo]

  def routesWithIds(routeIds: Seq[Long]): Seq[RouteInfo]

  def routeReferences(routeId: Long, stale: Boolean = true): RouteReferences

  def filterKnown(routeIds: Set[Long]): Set[Long]
}
