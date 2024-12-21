package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.common.Bounds
import kpn.api.common.route.RoutePath
import kpn.api.common.route.RouteSegment
import kpn.api.common.route.RouteStructureRow
import kpn.core.doc.RouteDetailDoc
import kpn.server.analyzer.engine.context.PreconditionMissingException

case class RouteAnalysisContext(
  routeDetailDoc: RouteDetailDoc,
  _routeIds: Option[Seq[Long]] = None,
  _bounds: Option[Option[Bounds]] = None,
  _structureRows: Option[Seq[RouteStructureRow]] = None,
  _segments: Option[Seq[RouteSegment]] = None,
  _paths: Option[Seq[RoutePath]] = None
) {
  def routeIds: Seq[Long] = _routeIds.getOrElse(throw new PreconditionMissingException)

  def bounds: Option[Bounds] = _bounds.getOrElse(throw new PreconditionMissingException)

  def structureRows: Seq[RouteStructureRow] = _structureRows.getOrElse(throw new PreconditionMissingException)

  def segments: Seq[RouteSegment] = _segments.getOrElse(throw new PreconditionMissingException)

  def paths: Seq[RoutePath] = _paths.getOrElse(throw new PreconditionMissingException)
}
