package kpn.server.repository

import kpn.api.common.Bounds
import kpn.api.common.Country
import kpn.api.common.RouteType
import kpn.api.common.common.Reference
import kpn.api.common.route.RouteInfo
import kpn.api.common.search.ConditionGroup
import kpn.api.common.search.RouteList
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.NetworkRouteDetail
import kpn.core.doc.ParentRouteData
import kpn.core.doc.RouteDoc
import kpn.core.doc.RouteRelation
import kpn.core.doc.SubRouteData
import kpn.core.doc.SuperSubSegmentInfo
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileInfo
import kpn.server.analyzer.engine.changes.changes.ReferencedElementIds
import kpn.server.analyzer.engine.tiles.domain.TileId
import kpn.server.api.analysis.pages.route.RouteMapData
import org.locationtech.jts.geom.Coordinate

trait RouteRepository {

  def allRouteIds(): Seq[Long]

  def activeRouteIds(): Seq[Long]

  def activeBaseRouteIds(): Seq[Long]

  def tileIds(routeType: RouteType): Seq[TileId]

  def saveRoute(route: RouteDoc): Unit

  def saveRouteTile(routeTileInfo: RouteTileInfo): Unit

  def routeTiles(routeId: Long): Seq[RouteTileInfo]

  def routeTileIds(routeId: Long): Seq[String]

  def tileInfosByZoomLevel(routeType: RouteType, zoomLevel: Int): Seq[RouteTileInfo]

  def tileInfosByTileId(routeType: RouteType, tileId: TileId): Seq[RouteTileInfo]

  def deleteRouteTiles(routeId: Long): Unit

  def deleteRouteTile(tileId: String): Unit

  def bulkSaveRoutes(routes: Seq[RouteDoc]): Unit

  def findRouteById(routeId: Long): Option[RouteDoc]

  def mapData(routeId: Long): Option[RouteMapData]

  def routeInfo(routeId: Long): Option[RouteInfo]

  def routeSegmentCount(routeId: Long): Option[Long]

  def networkReferences(routeId: Long): Seq[Reference]

  def routeCountry(routeId: Long): Option[Country]

  def explore(query: ConditionGroup): RouteList

  def activeRouteElementIds(): Seq[ReferencedElementIds]

  def saveBaseRoute(baseRoute: BaseRouteDoc): Unit

  def bulkSaveBaseRoutes(baseRoutes: Seq[BaseRouteDoc]): Unit

  def findBaseRouteById(routeId: Long): Option[BaseRouteDoc]

  def filterKnownBaseRoutes(routeIds: Set[Long]): Set[Long]

  def bounds(routeIds: Seq[Long]): Option[Bounds]

  def segments(routeIds: Seq[Long]): Seq[SuperSubSegmentInfo]

  def subRouteData(routeId: Long): Option[SubRouteData]

  def parentRoutes(routeId: Long): Seq[ParentRouteData]

  def networkRouteDetails(routeIds: Seq[Long]): Seq[NetworkRouteDetail]

  def subRelationTree(routeId: Long): Option[RouteRelation]

  def coordinatesArrays(routeIds: Seq[Long]): Seq[Array[Coordinate]]
}
