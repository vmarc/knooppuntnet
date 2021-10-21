package kpn.server.repository

import kpn.api.common.common.Reference
import kpn.api.common.route.RouteMapInfo
import kpn.api.common.route.RouteNameInfo
import kpn.api.custom.NetworkType
import kpn.core.doc.RouteDoc
import kpn.server.analyzer.engine.changes.changes.ReferencedElementIds
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo

trait RouteRepository {

  def allRouteIds(): Seq[Long]

  def activeRouteIds(): Seq[Long]

  def activeRouteElementIds(): Seq[ReferencedElementIds]

  def save(route: RouteDoc): Unit

  def bulkSave(routes: Seq[RouteDoc]): Unit

  def findById(routeId: Long): Option[RouteDoc]

  def mapInfo(routeId: Long): Option[RouteMapInfo]

  def nameInfo(routeId: Long): Option[RouteNameInfo]

  def networkReferences(routeId: Long): Seq[Reference]

  def filterKnown(routeIds: Set[Long]): Set[Long]

  def routeTileInfosByNetworkType(networkType: NetworkType): Seq[RouteTileInfo]

  def routeTileInfosById(routeId: Long): Option[RouteTileInfo]

}
