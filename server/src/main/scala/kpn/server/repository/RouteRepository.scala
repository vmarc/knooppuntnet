package kpn.server.repository

import kpn.api.common.Bounds
import kpn.api.common.Country
import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.api.common.route.RouteMapInfo
import kpn.api.common.route.RouteNameInfo
import kpn.api.common.search.ConditionGroup
import kpn.api.common.search.RouteList
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.ParentRouteData
import kpn.core.doc.RouteDoc
import kpn.core.doc.SubRouteData
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import kpn.server.analyzer.engine.changes.changes.ReferencedElementIds
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileId

trait RouteRepository {

  def allRouteIds(): Seq[Long]

  def activeRouteIds(): Seq[Long]

  def activeBaseRouteIds(): Seq[Long]

  def tiles(routeType: RouteType): Seq[TileId]

  def tilesWithName(routeType: RouteType, tileId: TileId): Seq[RouteTileDoc]

  def saveRoute(route: RouteDoc): Unit

  def saveRouteTile(routeTile: RouteTileDoc): Unit

  def routeTiles(routeId: Long): Seq[RouteTileDoc]

  def deleteRouteTiles(routeId: Long): Unit

  def bulkSaveRoutes(routes: Seq[RouteDoc]): Unit

  def findRouteById(routeId: Long): Option[RouteDoc]

  def mapInfo(routeId: Long): Option[RouteMapInfo]

  def nameInfo(routeId: Long): Option[RouteNameInfo]

  def networkReferences(routeId: Long): Seq[Reference]

  def routeTileInfosById(routeId: Long): Option[RouteTileInfo]

  def routeCountry(routeId: Long): Option[Country]

  def explore(query: ConditionGroup): RouteList

  def activeRouteElementIds(): Seq[ReferencedElementIds]

  def saveBaseRoute(baseRoute: BaseRouteDoc): Unit

  def bulkSaveBaseRoutes(baseRoutes: Seq[BaseRouteDoc]): Unit

  def findBaseRouteById(routeId: Long): Option[BaseRouteDoc]

  def filterKnownBaseRoutes(routeIds: Set[Long]): Set[Long]

  def routeTileInfosByRouteType(routeType: RouteType, nodeNetwork: Boolean): Seq[RouteTileInfo]

  def bounds(routeIds: Seq[Long]): Option[Bounds]

  def subRouteData(routeId: Long): Option[SubRouteData]

  def parentRoutes(routeId: Long): Seq[ParentRouteData]
}
