package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.common.Bounds
import kpn.core.doc.RouteDetailDoc
import kpn.server.analyzer.engine.context.PreconditionMissingException

case class RouteAnalysisContext(
  routeDetailDoc: RouteDetailDoc,
  _routeIds: Option[Seq[Long]] = None,
  _bounds: Option[Option[Bounds]] = None,
) {
  def routeIds: Seq[Long] = _routeIds.getOrElse(throw new PreconditionMissingException)

  def bounds: Option[Bounds] = _bounds.getOrElse(throw new PreconditionMissingException)
}
