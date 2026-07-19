package kpn.server.analyzer.engine.analysis.network.base.analyzers

import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.data.raw.RawRelation
import kpn.server.analyzer.engine.context.PreconditionMissingException

case class BaseNetworkAnalysisContext(
  relation: RawRelation,
  _name: Option[Option[String]] = None,
  _routeType: Option[RouteType] = None,
  _routeScope: Option[RouteScope] = None,
  abort: Boolean = false
) {

  def name: Option[String] = _name.getOrElse(throw new PreconditionMissingException)

  def routeType: RouteType = _routeType.getOrElse(throw new PreconditionMissingException)

  def routeScope: RouteScope = _routeScope.getOrElse(throw new PreconditionMissingException)
}
