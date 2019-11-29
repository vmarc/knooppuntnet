package kpn.server.repository

import akka.util.Timeout
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteReferences
import kpn.core.db.couch.Couch

trait RouteRepository {

  def save(routes: RouteInfo*): Unit

  def delete(routeIds: Seq[Long]): Unit

  def routeWithId(routeId: Long, timeout: Timeout = Couch.defaultTimeout): Option[RouteInfo]

  def routesWithIds(routeIds: Seq[Long], timeout: Timeout): Seq[RouteInfo]

  def routeReferences(routeId: Long, timeout: Timeout, stale: Boolean = true): RouteReferences

  def filterKnown(routeIds: Set[Long]): Set[Long]
}
