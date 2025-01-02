package kpn.server.repository

import kpn.api.common.Country
import kpn.api.common.NetworkType
import kpn.api.common.common.Reference
import kpn.api.common.route.RouteMapInfo
import kpn.api.common.route.RouteNameInfo
import kpn.api.common.search.ConditionGroup
import kpn.api.common.search.RouteSearchResult
import kpn.core.doc.RouteDoc
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileId

trait RouteRepository {

  def allRouteIds(): Seq[Long]

  def activeRouteIds(): Seq[Long]

  def tiles(networkType: NetworkType): Seq[TileId]

  def tilesWithName(networkType: NetworkType, tileId: TileId): Seq[RouteTileDoc]

  def saveRoute(route: RouteDoc): Unit

  def saveRouteTile(routeTile: RouteTileDoc): Unit

  def bulkSaveRoutes(routes: Seq[RouteDoc]): Unit

  def findRouteById(routeId: Long): Option[RouteDoc]

  def mapInfo(routeId: Long): Option[RouteMapInfo]

  def nameInfo(routeId: Long): Option[RouteNameInfo]

  def networkReferences(routeId: Long): Seq[Reference]

  def routeTileInfosById(routeId: Long): Option[RouteTileInfo]

  def routeCountry(routeId: Long): Option[Country]

  def explore(query: ConditionGroup): Seq[RouteSearchResult]
}
