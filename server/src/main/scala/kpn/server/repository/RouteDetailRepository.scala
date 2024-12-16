package kpn.server.repository

import kpn.api.common.Bounds
import kpn.api.common.NetworkType
import kpn.core.doc.RouteDetailDoc
import kpn.server.analyzer.engine.changes.changes.ReferencedElementIds
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo

trait RouteDetailRepository {

  def activeRouteElementIds(): Seq[ReferencedElementIds]

  def save(routeDetail: RouteDetailDoc): Unit

  def bulkSave(routeDetails: Seq[RouteDetailDoc]): Unit

  def findById(routeId: Long): Option[RouteDetailDoc]

  def filterKnown(routeIds: Set[Long]): Set[Long]

  def routeTileInfosByNetworkType(networkType: NetworkType, nodeNetwork: Boolean): Seq[RouteTileInfo]

  def bounds(routeIds: Seq[Long]): Option[Bounds]
}
