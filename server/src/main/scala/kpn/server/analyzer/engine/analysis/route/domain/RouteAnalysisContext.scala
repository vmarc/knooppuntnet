package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.common.Bounds
import kpn.api.common.route.ParentRoute
import kpn.api.common.route.RoutePath
import kpn.api.common.route.RouteSegment
import kpn.api.common.route.RouteStructureRow
import kpn.core.doc.BaseRouteDoc
import kpn.server.analyzer.engine.context.PreconditionMissingException

case class RouteAnalysisContext(
  route: BaseRouteDoc,
  _routeIds: Option[Seq[Long]] = None,
  _bounds: Option[Option[Bounds]] = None,
  _structureRows: Option[Seq[RouteStructureRow]] = None,
  _distance: Option[Long] = None,
  _segments: Option[Seq[RouteSegment]] = None,
  _paths: Option[Seq[RoutePath]] = None,
  _parentRoutes: Option[Seq[ParentRoute]] = None
) {
  def routeIds: Seq[Long] = _routeIds.getOrElse(throw new PreconditionMissingException)

  def bounds: Option[Bounds] = _bounds.getOrElse(throw new PreconditionMissingException)

  def structureRows: Seq[RouteStructureRow] = _structureRows.getOrElse(throw new PreconditionMissingException)

  def distance: Long = _distance.getOrElse(throw new PreconditionMissingException)

  def segments: Seq[RouteSegment] = _segments.getOrElse(throw new PreconditionMissingException)

  def paths: Seq[RoutePath] = _paths.getOrElse(throw new PreconditionMissingException)

  def parentRoutes: Seq[ParentRoute] = _parentRoutes.getOrElse(throw new PreconditionMissingException)
}
