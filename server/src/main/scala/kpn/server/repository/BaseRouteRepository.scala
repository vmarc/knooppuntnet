package kpn.server.repository

import kpn.api.common.Bounds
import kpn.api.common.RouteType
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.ParentRouteData
import kpn.core.doc.SubRouteData
import kpn.server.analyzer.engine.changes.changes.ReferencedElementIds
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo

trait BaseRouteRepository {

  def activeRouteElementIds(): Seq[ReferencedElementIds]

  def save(baseRoute: BaseRouteDoc): Unit

  def bulkSave(baseRoutes: Seq[BaseRouteDoc]): Unit

  def findById(routeId: Long): Option[BaseRouteDoc]

  def filterKnown(routeIds: Set[Long]): Set[Long]

  def routeTileInfosByrouteType(routeType: RouteType, nodeNetwork: Boolean): Seq[RouteTileInfo]

  def bounds(routeIds: Seq[Long]): Option[Bounds]

  def subRouteData(routeId: Long): Option[SubRouteData]

  def parentRoutes(routeId: Long): Seq[ParentRouteData]
}
