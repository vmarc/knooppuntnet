package kpn.server.repository

import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteReferences
import kpn.server.analyzer.engine.changes.changes.RouteElements

trait RouteRepository {

  def allRouteIds(): Seq[Long]

  def save(routes: RouteInfo): Unit

  def saveElements(routeElements: RouteElements): Unit

  def delete(routeId: Long): Unit

  def routeWithId(routeId: Long): Option[RouteInfo]

  def routeElementsWithId(routeId: Long): Option[RouteElements]

  def routeReferences(routeId: Long, stale: Boolean = true): RouteReferences

  def filterKnown(routeIds: Set[Long]): Set[Long]
}
