package kpn.server.repository

import kpn.api.common.common.Reference
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteMapInfo
import kpn.api.common.route.RouteNameInfo
import kpn.server.analyzer.engine.changes.changes.RouteElements

trait RouteRepository {

  def allRouteIds(): Seq[Long]

  def activeRouteIds(): Seq[Long]

  def save(route: RouteInfo): Unit

  def bulkSave(routes: Seq[RouteInfo]): Unit

  def saveElements(routeElements: RouteElements): Unit

  def delete(routeId: Long): Unit

  def findById(routeId: Long): Option[RouteInfo]

  def mapInfo(routeId: Long): Option[RouteMapInfo]

  def nameInfo(routeId: Long): Option[RouteNameInfo]

  def routeElementsWithId(routeId: Long): Option[RouteElements]

  def networkReferences(routeId: Long, stale: Boolean = true): Seq[Reference]

  def filterKnown(routeIds: Set[Long]): Set[Long]
}
