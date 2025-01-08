package kpn.server.repository

import kpn.api.common.Bounds
import kpn.api.common.RouteType
import kpn.core.doc.ParentRouteData
import kpn.core.doc.RouteDetailDoc
import kpn.core.doc.SubRouteData
import kpn.server.analyzer.engine.changes.changes.ReferencedElementIds
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo

trait RouteDetailRepository {

  def activeRouteElementIds(): Seq[ReferencedElementIds]

  def save(routeDetail: RouteDetailDoc): Unit

  def bulkSave(routeDetails: Seq[RouteDetailDoc]): Unit

  def findById(routeId: Long): Option[RouteDetailDoc]

  def filterKnown(routeIds: Set[Long]): Set[Long]

  def routeTileInfosByrouteType(routeType: RouteType, nodeNetwork: Boolean): Seq[RouteTileInfo]

  def bounds(routeIds: Seq[Long]): Option[Bounds]

  def subRouteData(routeId: Long): Option[SubRouteData]

  def parentRoutes(routeId: Long): Seq[ParentRouteData]
}
